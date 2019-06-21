package me.galaxy.rocket.annotation;

import me.galaxy.rocket.config.ExceptionIgnore;
import me.galaxy.rocket.config.NoException;
import me.galaxy.rocket.config.NoIgnore;
import me.galaxy.rocket.config.NoRPCHook;
import org.apache.rocketmq.remoting.RPCHook;

import java.lang.annotation.*;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 14:11
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RocketListener {

    boolean orderly() default false;

    String nameServer() default "";

    String instance() default "";

    String consumerGroup();

    String topic() default "";

    String tag();

    int delayTimeLevel() default Integer.MIN_VALUE;

    Class<? extends Throwable>[] ignoredExceptions() default NoException.class;

    Class<? extends ExceptionIgnore>[] exceptionIgnores() default NoIgnore.class;

    Class<? extends RPCHook> hook() default NoRPCHook.class;

}
