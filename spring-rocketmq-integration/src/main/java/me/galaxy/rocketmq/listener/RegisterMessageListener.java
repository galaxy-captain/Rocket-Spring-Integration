package me.galaxy.rocketmq.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 11:23
 **/
public class RegisterMessageListener implements MessageListenerConcurrently {

    private Object consumerClass;

    private Method consumerMethod;

    protected String key;

    public RegisterMessageListener(Object consumerClass, Method consumerMethod, String key) {
        this.consumerClass = consumerClass;
        this.consumerMethod = consumerMethod;
        this.key = key;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageList, ConsumeConcurrentlyContext context) {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
