package me.galaxy.rocketmq.annotation;

import me.galaxy.rocketmq.exception.NoException;

import java.lang.annotation.*;

/**
 * @author galaxy-captain
 * @description
 * @date 2019-06-17 14:00
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RocketListener {

    String nameServer() default "";

    String instance() default "";

    String consumerGroup() default "";

    String topic() default "";

    String tag() default "*";

    String key() default "*";

    int delayTimeLevel() default Integer.MIN_VALUE;

    Class<? extends Throwable>[] ignoredExceptions() default NoException.class;

}
