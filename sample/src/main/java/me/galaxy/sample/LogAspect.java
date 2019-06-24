package me.galaxy.sample;

import java.lang.annotation.*;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-24 15:27
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogAspect {
}
