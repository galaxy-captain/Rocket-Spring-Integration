package me.galaxy.rocketmq.listener.wrapper;

import me.galaxy.rocketmq.listener.RegisterMessageListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 21:03
 **/
public abstract class RetryDelayTimeMessageListenerWrapper extends RegisterMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RetryDelayTimeMessageListenerWrapper.class);

    public RetryDelayTimeMessageListenerWrapper(Object consumerClass, Method consumerMethod, String key, int delayLevel, Class<? extends Throwable>[] ignorableExceptions) {
        super(consumerClass, consumerMethod, key, delayLevel, ignorableExceptions);
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageList, ConsumeConcurrentlyContext context) {

        // consumer设置延迟重试的时间
        context.setDelayLevelWhenNextConsume(this.delayLevel);

        return retryDelayConsumerMessageWrapper(messageList, context);
    }

    protected abstract ConsumeConcurrentlyStatus retryDelayConsumerMessageWrapper(List<MessageExt> messageList, ConsumeConcurrentlyContext context);

}
