package me.galaxy.rocket.annotation;

import me.galaxy.rocket.config.ExceptionIgnore;
import me.galaxy.rocket.config.NoException;
import me.galaxy.rocket.config.NoIgnore;

import java.lang.annotation.*;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 14:11
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RocketConsumer {

    String nameServer() default "";

    String topic() default "";

    int retryTimes() default Integer.MIN_VALUE;

    int delayTimeLevel() default Integer.MIN_VALUE;

    int suspendTimeMillis() default Integer.MIN_VALUE;

    Class<? extends Throwable>[] ignoredExceptions() default NoException.class;

    Class<? extends ExceptionIgnore>[] exceptionIgnores() default NoIgnore.class;

}
