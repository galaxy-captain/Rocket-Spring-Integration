package me.galaxy.rocketmq.listener.wrapper;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 21:35
 **/
public class MessageListenerConcurrentlyWrapper extends ConvertMessageListenerWrapper {

    public MessageListenerConcurrentlyWrapper(Object consumerClass, Method consumerMethod, String key, int delayLevel, Class<? extends Throwable>[] ignoredExceptions) {
        super(consumerClass, consumerMethod, key, delayLevel, ignoredExceptions);
    }

}
