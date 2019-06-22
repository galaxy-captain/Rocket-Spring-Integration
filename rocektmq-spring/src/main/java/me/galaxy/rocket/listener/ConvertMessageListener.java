package me.galaxy.rocket.listener;

import me.galaxy.rocket.config.ConsumerConfig;
import me.galaxy.rocket.exception.NoMethodParameterException;
import me.galaxy.rocket.utils.MessageConverter;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 21:44
 **/
public class ConvertMessageListener extends IgnoredExceptionListener {

    private static final Logger logger = LoggerFactory.getLogger(ConvertMessageListener.class);

    private Class<?> convertToClass;

    private boolean isConvertToList = false;

    private boolean hasMessageExt = false;

    private boolean isConcurrently = false;

    private boolean isOrderly = false;

    public ConvertMessageListener(Object consumerClass, Method consumerMethod, ConsumerConfig config) throws NoMethodParameterException {
        super(consumerClass, consumerMethod, config);

        // 初始化调用方法
        this.initMethodInvokeAction(consumerMethod);
    }

    @Override
    protected Object ignoredExceptionConsumerMessageWrapper(List<MessageExt> messageList, ConsumeConcurrentlyContext concurrentlyContext, ConsumeOrderlyContext orderlyContext) {
        return invokeMethod(consumerClass, consumerMethod, messageList, concurrentlyContext, orderlyContext);
    }

    private Object invokeMethod(Object object,
                                Method method,
                                List<MessageExt> messageList,
                                ConsumeConcurrentlyContext concurrentlyContext,
                                ConsumeOrderlyContext orderlyContext) {

        // 获取需要注入的数据类型
        detectConvertClass(method);

        Object injectClassObject;

        if (this.isConvertToList) {

            List realList = new ArrayList(messageList.size());

            for (MessageExt messageExt : messageList) {
                String msg = new String(messageExt.getBody());
                realList.add(convertToClass(msg));
            }

            injectClassObject = realList;
        } else {
            String msg = new String(messageList.get(0).getBody());
            injectClassObject = convertToClass(msg);
        }

        return invokeMethodWithAllParameters(object, method, injectClassObject, messageList, concurrentlyContext, orderlyContext);
    }

    private Object invokeMethodWithAllParameters(Object object,
                                                 Method method,
                                                 Object injectObject,
                                                 List<MessageExt> messageList,
                                                 ConsumeConcurrentlyContext concurrentlyContext,
                                                 ConsumeOrderlyContext orderlyContext) {
        try {

            if (isConvertToList) {
                if (hasMessageExt) {
                    if (isConcurrently) {
                        return method.invoke(object, injectObject, messageList, concurrentlyContext);
                    } else if (isOrderly) {
                        return method.invoke(object, injectObject, messageList, orderlyContext);
                    } else {
                        return method.invoke(object, injectObject, messageList);
                    }
                } else {
                    if (isConcurrently) {
                        return method.invoke(object, injectObject, concurrentlyContext);
                    } else if (isOrderly) {
                        return method.invoke(object, injectObject, orderlyContext);
                    } else {
                        return method.invoke(object, injectObject);
                    }
                }
            } else {
                if (hasMessageExt) {
                    if (isConcurrently) {
                        return method.invoke(object, injectObject, messageList.get(0), concurrentlyContext);
                    } else if (isOrderly) {
                        return method.invoke(object, injectObject, messageList.get(0), orderlyContext);
                    } else {
                        return method.invoke(object, injectObject, messageList.get(0));
                    }
                } else {
                    if (isConcurrently) {
                        return method.invoke(object, injectObject, concurrentlyContext);
                    } else if (isOrderly) {
                        return method.invoke(object, injectObject, orderlyContext);
                    } else {
                        return method.invoke(object, injectObject);
                    }
                }
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    private void initMethodInvokeAction(Method method) throws NoMethodParameterException {

        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length == 0) {
            throw new NoMethodParameterException(
                    String.format(
                            "初始化%s[%s]失败，方法参数必须包含需要的数据类型",
                            this.consumerClass.getClass().getName(),
                            this.consumerMethod.getName()
                    )
            );
        }

        // 方法中只有数据类型时，直接调用
        if (parameterTypes.length == 1) {
            return;
        }

        // 第二个参数是context
        if (parameterTypes[1] == ConsumeConcurrentlyContext.class) {
            this.isConcurrently = true;
            return;
        } else if (parameterTypes[1] == ConsumeOrderlyContext.class) {
            this.isOrderly = true;
            return;
        }

        this.hasMessageExt = true;

        // 只有两个参数
        if (parameterTypes.length == 2) {
            return;
        }

        // 共有三个参数
        if (parameterTypes[2] == ConsumeConcurrentlyContext.class) {
            this.isConcurrently = true;
        } else if (parameterTypes[2] == ConsumeOrderlyContext.class) {
            this.isOrderly = true;
        }

    }

    /**
     * 将消息转换为需要的类型
     */
    private Object convertToClass(String msg) {

        if (this.convertToClass == String.class) {
            return msg;
        }

        return MessageConverter.convertJSONToObject(msg, this.convertToClass);
    }

    /**
     * 获取需要转换的类型
     */
    private void detectConvertClass(Method method) {
        Class<?> parameterType = method.getParameterTypes()[0];
        if (parameterType == List.class) {
            this.isConvertToList = true;
            this.convertToClass = null;
        } else {
            this.convertToClass = parameterType;
        }
    }

}
