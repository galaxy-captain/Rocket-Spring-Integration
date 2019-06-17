package me.galaxy.rocketmq;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-06-17 13:57
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Import(RocketBootstrap.class)
public @interface EnableRocket {

}
