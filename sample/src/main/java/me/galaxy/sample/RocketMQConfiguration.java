package me.galaxy.sample;

import me.galaxy.rocket.annotation.EnableRocketMQ;
import me.galaxy.rocketmq.RocketConfigurationBean;
import me.galaxy.rocketmq.annotation.EnableRocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 13:11
 **/
@EnableRocketMQ
@Configuration
public class RocketMQConfiguration {

    @PostConstruct
    public void postConstruct() {
        System.out.println("post construct");
    }

    @Bean
    public RocketConfigurationBean rocketConfiguration() {

        RocketConfigurationBean rocketConfiguration = new RocketConfigurationBean();
        rocketConfiguration.setNameServer("127.0.0.1:9876");

        return rocketConfiguration;
    }

}
