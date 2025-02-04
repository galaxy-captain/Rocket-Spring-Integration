package me.galaxy.rocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 14:00
 **/
public class RocketMQAutoConfiguration {

    @Bean
    public RocketAnnotationBeanPostProcessor rocketAnnotationBeanPostProcessor() {
        return new RocketAnnotationBeanPostProcessor();
    }

    @Bean
    public RocketConsumerSpringLifecycle rocketConsumerLifecycle() {
        return new RocketConsumerSpringLifecycle();
    }

}
