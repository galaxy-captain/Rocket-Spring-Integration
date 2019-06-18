package me.galaxy.rocketmq.annotation;

import me.galaxy.rocketmq.RocketBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-17 13:57
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Import(RocketBootstrap.class)
public @interface EnableRocket {

}
