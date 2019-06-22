package me.galaxy.rocketmq.listener.wrapper;

import com.alibaba.fastjson.JSONException;
import me.galaxy.rocketmq.listener.RegisterMessageListener;
import me.galaxy.rocketmq.utils.MessageConverter;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 21:03
 **/
public class ConvertMessageListenerWrapper extends IgnoredExceptionListenerWrapper {

    private static final Logger logger = LoggerFactory.getLogger(ConvertMessageListenerWrapper.class);

    public ConvertMessageListenerWrapper(Object consumerClass, Method consumerMethod, String key, int delayLevel, Class<? extends Throwable>[] ignoredExceptions) {
        super(consumerClass, consumerMethod, key, delayLevel, ignoredExceptions);
    }

    @Override
    protected ConsumeConcurrentlyStatus ignoredExceptionConsumerMessageWrapper(List<MessageExt> messageList, ConsumeConcurrentlyContext context) {

        try {
            return invokeMethod(this.consumerClass, this.consumerMethod, messageList, context);
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {

            String em = String.format(
                    "调用[%s]:%s(List<MessageExt>,ConsumeConcurrentlyContext)失败",
                    consumerClass.getClass().getName(),
                    consumerMethod.getName()
            );

            logger.error(em, e);

            throw new RuntimeException(em, e);
        }

    }

    private ConsumeConcurrentlyStatus invokeMethod(Object o,
                                                   Method m,
                                                   List<MessageExt> ms,
                                                   ConsumeConcurrentlyContext context)
            throws InvocationTargetException, IllegalAccessException {

        for (MessageExt messageExt : ms) {

            String strBody = new String(messageExt.getBody());

            try {
                Object obj = MessageConverter.convertJSONToObject(strBody, this.convertToClass);
                Object status = m.invoke(o, obj, messageExt, context);

                ConsumeConcurrentlyStatus result = determineBackStatus(status);

                if (result == ConsumeConcurrentlyStatus.RECONSUME_LATER) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

            } catch (JSONException e) {
                String em = String.format("JSON转换Class失败，json=%s,class=%s", strBody, this.convertToClass.getName());
                logger.error(em, e);
                throw e;
            }

        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    private ConsumeConcurrentlyStatus determineBackStatus(Object status) {

        if (status == null) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        if (status instanceof Boolean) {
            return ((Boolean) status) ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

        if (status instanceof ConsumeConcurrentlyStatus) {
            return (ConsumeConcurrentlyStatus) status;
        }

        throw new RegisterMessageListener.IllegalRocketListenerStatusException("非法的RocketListener返回状态");

    }

}
