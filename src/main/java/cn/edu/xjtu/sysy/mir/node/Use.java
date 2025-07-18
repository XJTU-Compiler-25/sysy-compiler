package cn.edu.xjtu.sysy.mir.node;

// User 引用的 Value 都应该以 Use 间接引用，以便 replaceAllUsesWith
public final class Use<V extends Value> {
    public User user;
    public V value;

    Use(User user, V value) {
        this.user = user;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return user.hashCode() ^ (value.hashCode() << 7);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Use<?> use &&
                user.equals(use.user) &&
                value.equals(use.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public void replaceUser(User newUser) {
        user.removeUsed(this);
        user = newUser;
        newUser.addUsed(this);
    }

    public void replaceValue(V newValue) {
        value.removeUse(this);
        value = newValue;
        newValue.addUse(this);
    }

    public void dispose() {
        user.removeUsed(this);
        value.removeUse(this);
    }
}
