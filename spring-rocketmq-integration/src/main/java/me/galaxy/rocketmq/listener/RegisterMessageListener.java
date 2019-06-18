package me.galaxy.rocketmq.listener;

import com.alibaba.fastjson.JSONException;
import me.galaxy.rocketmq.utils.MessageConverter;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 11:23
 **/
public abstract class RegisterMessageListener implements MessageListenerConcurrently {

    private static final Logger logger = LoggerFactory.getLogger(RegisterMessageListener.class);

    protected Object consumerClass;

    protected Method consumerMethod;

    protected String key;

    protected int delayLevel;

    protected Class<?> convertToClass;

    protected Class<? extends Throwable>[] ignorableExceptions;

    public RegisterMessageListener(Object consumerClass, Method consumerMethod, String key, int delayLevel, Class<? extends Throwable>[] ignorableExceptions) {
        this.consumerClass = consumerClass;
        this.consumerMethod = consumerMethod;
        this.key = key;
        this.delayLevel = delayLevel;
        this.ignorableExceptions = ignorableExceptions;

        this.consumerMethod.setAccessible(true);
        this.convertToClass = this.consumerMethod.getParameterTypes()[0];
    }

    public class IllegalRocketListenerParameterException extends RuntimeException {
        public IllegalRocketListenerParameterException(String s) {
            super(s);
        }
    }

    public class IllegalRocketListenerStatusException extends RuntimeException {
        public IllegalRocketListenerStatusException(String s) {
            super(s);
        }
    }

}
