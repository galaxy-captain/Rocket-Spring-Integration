package me.galaxy.rocket.listener;

import me.galaxy.rocket.config.ConsumerConfig;
import me.galaxy.rocket.config.ExceptionIgnore;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 19:10
 **/
public abstract class AbstractMessageListener implements MessageListenerConcurrently, MessageListenerOrderly {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageListener.class);

    protected Object consumerClass;

    protected Method consumerMethod;

    protected Class<? extends Throwable>[] ignorableExceptions;

    protected ExceptionIgnore[] exceptionIgnores;

    /**
     * MessageListenerConcurrently消费端控制延迟
     */
    private int delayLevelWhenNextConsume;

    // /**
    // *
    // */
    //private int ackIndex;

    /**
     * ConsumeOrderlyContext消费端控制暂停时间
     */
    private long suspendCurrentQueueTimeMillis;

    ///**
    // *
    // */
    //private boolean autoCommit;

    public AbstractMessageListener(Object consumerClass,
                                   Method consumerMethod,
                                   ConsumerConfig config) {

        this.consumerClass = consumerClass;
        this.consumerMethod = consumerMethod;
        this.ignorableExceptions = config.getIgnorableExceptions();
        this.exceptionIgnores = this.createExceptionIgnores(config.getExceptionIgnores());
        this.delayLevelWhenNextConsume = config.getDelayTimeLevel();
        this.suspendCurrentQueueTimeMillis = config.getSuspendTimeMillis();

        this.consumerMethod.setAccessible(true);
    }

    private ExceptionIgnore[] createExceptionIgnores(Class<? extends ExceptionIgnore>[] classes) {

        ExceptionIgnore[] exceptionIgnores = new ExceptionIgnore[classes.length];

        for (int i = 0; i < classes.length; i++) {

            try {
                exceptionIgnores[i] = classes[i].newInstance();
            } catch (InstantiationException | IllegalAccessException e) {

                if (logger.isErrorEnabled()) {
                    logger.error(
                            String.format(
                                    "初始化%s[%s]时，创建%s失败",
                                    this.consumerClass.getClass().getName(),
                                    this.consumerMethod.getName(),
                                    classes[i].getName()
                            )
                    );
                } else {
                    e.printStackTrace();
                }
            }

        }

        return exceptionIgnores;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {

        context.setDelayLevelWhenNextConsume(this.delayLevelWhenNextConsume);

        int msgLength = messages.size();

        // 执行下层逻辑
        Object result = consumeMessage(messages, context, null);

        return convertConcurrentlyConsumeResult(result, msgLength, context);
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messages, ConsumeOrderlyContext context) {

        context.setSuspendCurrentQueueTimeMillis(this.suspendCurrentQueueTimeMillis);

        // 执行下层逻辑
        Object result = consumeMessage(messages, null, context);

        return convertOrderlyConsumeResult(result);
    }

    private ConsumeConcurrentlyStatus convertConcurrentlyConsumeResult(Object result, int size, ConsumeConcurrentlyContext context) {

        // 返回值为void，表示消费成功，错误以异常抛出
        if (result == null) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        // 返回值为布尔型，true为成功，false为失败
        if (result instanceof Boolean) {
            return ((boolean) result) ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

        // 返回值为ConsumeConcurrentlyStatus
        if (result instanceof ConsumeConcurrentlyStatus) {
            return (ConsumeConcurrentlyStatus) result;
        }

        // 其他情况为异常情况，返回结果失败
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    private ConsumeOrderlyStatus convertOrderlyConsumeResult(Object result) {

        // 返回值为void，表示消费成功，错误以异常抛出
        if (result == null) {
            return ConsumeOrderlyStatus.SUCCESS;
        }

        // 返回值为布尔型，true为成功，false为失败
        if (result instanceof Boolean) {
            return ((boolean) result) ? ConsumeOrderlyStatus.SUCCESS : ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }

        // 返回值为ConsumeOrderlyStatus
        if (result instanceof ConsumeOrderlyStatus) {
            return (ConsumeOrderlyStatus) result;
        }

        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
    }

    protected abstract Object consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext concurrentlyContext, ConsumeOrderlyContext orderlyContext);

}
