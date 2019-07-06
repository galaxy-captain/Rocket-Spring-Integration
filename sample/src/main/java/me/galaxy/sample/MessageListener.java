package me.galaxy.sample;

import me.galaxy.rocket.annotation.RocketConsumer;
import me.galaxy.rocket.annotation.RocketListener;
import me.galaxy.rocket.exception.JSONConvertException;
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
@RocketConsumer(topic = "${mq.rocket.topic}")
@Service
public class MessageListener {


    // =================================== 单条消费 ===================================

    @LogAspect
    @RocketListener(consumerGroup = "consumerGroup1_1", tag = "tag_test_1")
    public ConsumeConcurrentlyStatus service1(String message) throws Exception {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @LogAspect
    @RocketListener(consumerGroup = "consumerGroup1_2", tag = "tag_test_2")
    public ConsumeConcurrentlyStatus service2(SimpleMessage message) throws Exception {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @LogAspect
    @RocketListener(consumerGroup = "consumerGroup1_3", tag = "tag_test_3")
    public ConsumeConcurrentlyStatus service3(SimpleMessage message, MessageExt messageExt) throws Exception {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @RocketListener(consumerGroup = "consumerGroup4", tag = "tag_test_4")
    public ConsumeConcurrentlyStatus service4(SimpleMessage message, MessageExt messageExt, ConsumeConcurrentlyContext context) throws Exception {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    // =================================== 多条消费 ===================================

    @LogAspect
    @RocketListener(consumerGroup = "consumerGroup2_1", topic = "topic_test_2", tag = "tag_test_1", maxBatchSize = 8)
    public ConsumeConcurrentlyStatus service4(List<String> messages) throws Exception {

        System.out.println(messages);

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @LogAspect
    @RocketListener(consumerGroup = "consumerGroup2_2", topic = "topic_test_2", tag = "tag_test_2", maxBatchSize = 8)
    public ConsumeConcurrentlyStatus service2(List<SimpleMessage> messages) throws Exception {

        System.out.println(messages);

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @LogAspect
    @RocketListener(consumerGroup = "consumerGroup2_3", topic = "topic_test_2", tag = "tag_test_3", maxBatchSize = 8)
    public ConsumeConcurrentlyStatus service3(List<SimpleMessage> messages, List<MessageExt> messageExts) throws Exception {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @LogAspect
    @RocketListener(consumerGroup = "consumerGroup2_4", topic = "topic_test_2", tag = "tag_test_4", maxBatchSize = 8)
    public ConsumeConcurrentlyStatus service4(List<SimpleMessage> messages, List<MessageExt> messageExts, ConsumeConcurrentlyContext context) throws Exception {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
