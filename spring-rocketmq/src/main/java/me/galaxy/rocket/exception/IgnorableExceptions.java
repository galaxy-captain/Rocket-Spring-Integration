package me.galaxy.rocket.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 需要忽略的异常的集合
 * @Author galaxy-captain
 * @Date 2019-07-06 14:11
 **/
public class IgnorableExceptions extends HashMap<Class<? extends Throwable>, Boolean> {

    public IgnorableExceptions() {
        super();
    }

    public IgnorableExceptions(Map<Class<? extends Throwable>, Boolean> map) {
        super(map);
    }

    public IgnorableExceptions(Class<? extends Throwable>[] exceptions) {
        this();

        if (exceptions != null && exceptions.length > 0) {
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
