package cn.edu.xjtu.sysy.mir.node;

// User 引用的 Value 都应该以 Use 间接引用，以便 replaceAllUsesWith
public final class Use<U extends User, V extends Value> {
    public U user;
    public V value;

    Use(U user, V value) {
        this.user = user;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return user.hashCode() ^ (value.hashCode() << 7);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Use use &&
                user.equals(use.user) &&
                value.equals(use.value);
    }

}
