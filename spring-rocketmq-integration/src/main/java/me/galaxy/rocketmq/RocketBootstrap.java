package me.galaxy.rocketmq;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-17 14:51
 **/
@Configuration
public class RocketBootstrap {

    @Bean
    public RocketListenerDetector rocketListenerDetector() {
        return new RocketListenerDetector();
    }

    @Bean
    public RocketInLifecycle rocketInLifecycle() {
        return new RocketInLifecycle();
    }

}
