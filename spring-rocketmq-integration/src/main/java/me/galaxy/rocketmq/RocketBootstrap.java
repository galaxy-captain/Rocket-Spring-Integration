package me.galaxy.rocketmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-06-17 14:51
 **/
@Configuration
public class RocketBootstrap {

    @Bean
//    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RocketListenerAnnotationPostProcessor rocketListenerAnnotationPostProcessor() {
        return new RocketListenerAnnotationPostProcessor();
    }

}
