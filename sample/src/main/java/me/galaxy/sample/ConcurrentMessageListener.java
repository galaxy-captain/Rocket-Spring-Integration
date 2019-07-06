package me.galaxy.sample;

import me.galaxy.rocket.annotation.RocketConsumer;
import me.galaxy.rocket.annotation.RocketListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import static me.galaxy.sample.RocketMQConstant.*;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-29 08:58
 **/
@RocketConsumer(topic = CONCURRENTLY_MESSAGE_TOPIC)
@Component
public class ConcurrentMessageListener {


    @RocketListener(consumerGroup = SINGLE_MESSAGE_LISTENER_CONSUMER_GROUP)
    public void singleMessageListener(String msg, MessageExt me, ConsumeConcurrentlyContext context) {
        System.out.println(msg);
    }

}
