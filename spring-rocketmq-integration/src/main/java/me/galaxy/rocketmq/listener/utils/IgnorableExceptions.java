package me.galaxy.rocketmq.listener.utils;

import me.galaxy.rocketmq.exception.NoException;

import java.util.HashMap;
import java.util.Map;

public class IgnorableExceptions extends HashMap<Class<? extends Throwable>, Boolean> {

    public IgnorableExceptions() {
        super();
    }

    public IgnorableExceptions(Map<Class<? extends Throwable>, Boolean> map) {
        super(map);
    }

    public IgnorableExceptions(Class<? extends Throwable>[] exceptions) {
        this();

        if (exceptions != null && exceptions.length > 0 && exceptions[0] != NoException.class) {
            addIgnorableExceptions(exceptions);
        }

    }

    /**
     * 添加可以被忽略的Exception异常
     *
     * @param exception {@code Exception}
     * @param ignore    {@code true}忽略  {@code false}不忽略
     */
    public void addException(Class<? extends Throwable> exception, boolean ignore) {
        put(exception, ignore);
    }

    public void addExceptions(Class<? extends Throwable>[] exceptions, boolean ignore) {
        for (Class<? extends Throwable> exception : exceptions) {
            put(exception, ignore);
        }
    }

    public void addIgnorableException(Class<? extends Throwable> exception) {
        put(exception, true);
    }

    public void addIgnorableExceptions(Class<? extends Throwable>[] exceptions) {
        for (Class<? extends Throwable> exception : exceptions) {
            put(exception, true);
        }
    }

}
