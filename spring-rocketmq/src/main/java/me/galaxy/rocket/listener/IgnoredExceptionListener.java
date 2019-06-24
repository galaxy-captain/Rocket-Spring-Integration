package me.galaxy.rocket.listener;

import me.galaxy.rocket.config.ConsumerConfig;
import me.galaxy.rocket.config.ExceptionIgnore;
import me.galaxy.rocket.exception.IgnorableExceptions;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.classify.BinaryExceptionClassifier;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 21:20
 **/
public abstract class IgnoredExceptionListener extends AbstractMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(IgnoredExceptionListener.class);

    protected IgnorableExceptions ignorableExceptions;

    protected BinaryExceptionClassifier classifier;

    public IgnoredExceptionListener(Object consumerClass,
                                    Method consumerMethod,
                                    ConsumerConfig config) {

        super(consumerClass, consumerMethod, config);

        this.ignorableExceptions = buildIgnorableExceptions(config.getIgnorableExceptions());
        this.classifier = new BinaryExceptionClassifier(this.ignorableExceptions, false);
    }

    @Override
    protected Object consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext concurrentlyContext, ConsumeOrderlyContext orderlyContext) {

        try {
            // 执行下层业务逻辑
            return ignoredExceptionConsumerMessageWrapper(messageExtList, concurrentlyContext, orderlyContext);
        } catch (Exception exception) {

            // 可以被忽略的异常
            if (this.classifier.classify(exception)) {
                return true;
            }

            // 根据异常的内容判断是否可以忽略
            if (ignorable(exception)) {
                return true;
            }

            // 打印异常信息
            if (logger.isErrorEnabled()) {
                logger.error(exception.getMessage());
            }

            // 异常导致消费失败
            return false;
        }

    }

    private IgnorableExceptions buildIgnorableExceptions(Class<? extends Throwable>[] exceptions) {
        return new IgnorableExceptions(exceptions);
    }

    private boolean ignorable(Exception e) {
        for (ExceptionIgnore exceptionIgnore : exceptionIgnores) {
            if (exceptionIgnore.ignorable(e)) {
                return true;
            }
        }
        return false;
    }

    protected abstract Object ignoredExceptionConsumerMessageWrapper(List<MessageExt> messageList, ConsumeConcurrentlyContext concurrentlyContext, ConsumeOrderlyContext orderlyContext) throws Exception;

}
