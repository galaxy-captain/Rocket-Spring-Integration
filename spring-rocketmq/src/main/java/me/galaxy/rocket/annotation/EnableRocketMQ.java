package me.galaxy.rocket.annotation;

import me.galaxy.rocket.RocketMQAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 12:09
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Import(RocketMQAutoConfiguration.class)
public @interface EnableRocketMQ {

}
