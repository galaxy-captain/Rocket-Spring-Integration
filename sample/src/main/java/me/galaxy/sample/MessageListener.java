package me.galaxy.sample;

import com.alibaba.fastjson.JSONException;
import me.galaxy.rocketmq.annotation.RocketListener;
import me.galaxy.rocketmq.annotation.RocketNameServer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 13:20
 **/
@RocketNameServer
@Service
public class MessageListener {

    @RocketListener(
            consumerGroup = "test_consumer_group",
            topic = "test_topic",
            tag = "test_tag",
            delayTimeLevel = 2,
            ignoredExceptions = JSONException.class
    )
    public ConsumeConcurrentlyStatus service(SimpleMessage message, MessageExt messageExt, ConsumeConcurrentlyContext context) {

        System.out.println(messageExt.getKeys());
        System.out.println(message.toString());

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
