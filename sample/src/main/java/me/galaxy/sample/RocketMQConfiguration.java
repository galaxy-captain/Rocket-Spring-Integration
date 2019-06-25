package me.galaxy.sample;

import me.galaxy.rocket.RocketConfiguration;
import me.galaxy.rocket.RocketTemplate;
import me.galaxy.rocket.annotation.EnableRocketMQ;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 13:11
 **/
@EnableRocketMQ
@Configuration
public class RocketMQConfiguration {

    @Bean
    public RocketConfiguration rocketConfiguration() {

        RocketConfiguration rocketConfiguration = new RocketConfiguration();
        rocketConfiguration.setNameServer("127.0.0.1:9876");

        return rocketConfiguration;
    }

    @Bean
    public DefaultMQProducer defaultMQProducer() {

        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setProducerGroup("producerGroup");

        return producer;
    }

    @Bean
    public RocketTemplate rocketTemplate(DefaultMQProducer producer) {
        return new RocketTemplate(producer);
    }

}
