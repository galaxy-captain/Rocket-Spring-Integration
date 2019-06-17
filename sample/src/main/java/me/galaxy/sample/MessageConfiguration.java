package me.galaxy.sample;

import me.galaxy.rocketmq.EnableRocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-06-17 18:04
 **/
@Configuration
public class MessageConfiguration {

    @Bean
    public MessageListener messageListener1() {
        return new MessageListener();
    }

    @Bean
    public MessageListener messageListener2() {
        return new MessageListener();
    }


}
