package me.galaxy.rocket.listener;

import me.galaxy.rocket.config.ConsumerConfig;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 21:44
 **/
public class ConvertMessageListener extends IgnoredExceptionListener {

    public ConvertMessageListener(Object consumerClass, Method consumerMethod, ConsumerConfig config) throws Exception {
        super(consumerClass, consumerMethod, config);
    }

    @Override
    protected Object ignoredExceptionConsumerMessageWrapper(List<MessageExt> messageList, MessageQueue queue) {
        return null;
    }

}
