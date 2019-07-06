package me.galaxy.sample;

import me.galaxy.rocket.RocketConfiguration;
import me.galaxy.rocket.RocketTemplate;
import me.galaxy.rocket.annotation.EnableRocket;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 13:11
 **/
@EnableRocket
@Configuration
public class RocketMQConfiguration {

    @Value("${mq.rocket.nameServer}")
    private String nameServer;

    @Bean
    public RocketConfiguration rocketConfiguration() {

        RocketConfiguration rocketConfiguration = new RocketConfiguration();
        rocketConfiguration.setRetryTimes(3);
        rocketConfiguration.setDelayTimeLevel(1);

        return rocketConfiguration;
    }

    @Bean
    public DefaultMQProducer defaultMQProducer() {

        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr(nameServer);
        producer.setProducerGroup("producerGroup");

        return producer;
    }

    @Bean
    public RocketTemplate rocketTemplate(DefaultMQProducer producer) {
        return new RocketTemplate(producer);
    }

    @Bean
    public MessageListenerLog messageListenerLog() {
        return new MessageListenerLog();
    }

}
