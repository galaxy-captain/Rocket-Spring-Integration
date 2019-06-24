package me.galaxy.sample;

import me.galaxy.rocket.annotation.RocketConsumer;
import me.galaxy.rocket.annotation.RocketListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 13:20
 **/
@RocketConsumer(topic = "myTestTopic")
@Service
public class MessageListener {

    @RocketListener(consumerGroup = "test_group_1", tag = "test_tag_1", orderly = true, suspendTimeMillis = 1000)
    public ConsumeOrderlyStatus service1(List<SimpleMessage> message, List<MessageExt> messageExt, ConsumeOrderlyContext context) throws Exception {

        System.out.println(message + "\n" + messageExt);

        return ConsumeOrderlyStatus.SUCCESS;
    }

    @LogAspect
    @RocketListener(consumerGroup = "test_group_2", tag = "test_tag_2", delayTimeLevel = 1)
    public ConsumeConcurrentlyStatus service2(SimpleMessage message, MessageExt messageExt, ConsumeConcurrentlyContext context) throws Exception {

        System.out.println(message + "\n" + messageExt);

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
