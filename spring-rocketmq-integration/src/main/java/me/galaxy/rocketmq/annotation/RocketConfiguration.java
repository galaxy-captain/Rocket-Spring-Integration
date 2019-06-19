package me.galaxy.rocketmq.annotation;

import me.galaxy.rocketmq.exception.NoException;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-19 09:55
 **/
public @interface RocketConfiguration {

    String nameServer() default "";

    int delayTimeLevel() default Integer.MIN_VALUE;

    Class<? extends Throwable>[] ignoredExceptions() default NoException.class;

}
