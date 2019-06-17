package me.galaxy.rocketmq;

import java.lang.annotation.*;

/**
 * @author duanxiaolei@bytedance.com
 * @description
 * @date 2019-06-17 14:00
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RocketListener {

    String template() default "";

    String consumerGroup() default "";

    String topic() default "";

    String tag() default "";

    String key() default "";

}
