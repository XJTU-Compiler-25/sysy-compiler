package cn.edu.xjtu.sysy;

import cn.edu.xjtu.sysy.error.ErrManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked"})
public final class Pipeline<T> {

    public final ErrManager errManager;
    private final Pass<T, ?>[] analyses;
    private final Pass<T, ?>[] transformers;
    private T processingObj;
    private final HashMap<Class<? extends Pass<T, ?>>, Object> cache = new HashMap<>();

    private Pipeline(ErrManager errManager,
                    Supplier<? extends Pass<T, ?>>[] analyses,
                    Supplier<? extends Pass<T, ?>>[] transformers) {
        this.errManager = errManager;
        this.analyses = Arrays.stream(analyses).map(Supplier::get).toArray(Pass[]::new);
        this.transformers = Arrays.stream(transformers).map(Supplier::get).toArray(Pass[]::new);
    }

    public void process(T input) {
        this.processingObj = input;
        cache.clear();
        for (var pass : transformers) {
            pass.setCurrentPipeline(this);
            pass.process(input);
            invalidateAll();
        }
    }

    public <E, P extends Pass<T, E>> E getResult(Class<P> clazz) {
        E result = (E) cache.get(clazz);
        if (result != null) return result;

        P analysis = (P) Arrays.stream(analyses).filter(clazz::isInstance).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No analysis found for " + clazz.getName()));
        analysis.setCurrentPipeline(this);
        result = analysis.process(processingObj);
        cache.put(clazz, result);
        return result;
    }

    public void invalidate(Class<? extends Pass<T, ?>> pass) {
        cache.remove(pass);
    }

    public void invalidate(Class<? extends Pass<T, ?>>... passes) {
        for (var pass : passes) invalidate(pass);
    }

    public void invalidateAll() {
        cache.clear();
    }

    public static <T> Builder<T> builder(Class<T> ignored) {
        return new Builder<>();
    }

    public static final class Builder<T> {
        private ErrManager errManager = ErrManager.GLOBAL;
        private final ArrayList<Supplier<? extends Pass<T, ?>>> analyses = new ArrayList<>();
        private final ArrayList<Supplier<? extends Pass<T, ?>>> transformers = new ArrayList<>();

        public Builder<T> withErrManager(ErrManager errManager) {
            this.errManager = errManager;
            return this;
        }

        public Builder<T> addAnalysis(Supplier<? extends Pass<T, ?>> analysis) {
            analyses.add(analysis);
            return this;
        }

        @SafeVarargs
        public final Builder<T> addAnalyses(Supplier<? extends Pass<T, ?>>... analyses) {
            for (var p : analyses) addAnalysis(p);
            return this;
        }

        public Builder<T> addTransformer(Supplier<? extends Pass<T, ?>> transformer) {
            transformers.add(transformer);
            return this;
        }

        @SafeVarargs
        public final Builder<T> addTransformers(Supplier<? extends Pass<T, ?>>... transformers) {
            for (var p : transformers) addTransformer(p);
            return this;
        }

        public Pipeline<T> build() {
            return new Pipeline<>(errManager, analyses.toArray(new Supplier[0]), transformers.toArray(new Supplier[0]));
        }
    }

}
