package me.galaxy.rocketmq.listener.wrapper;

import me.galaxy.rocketmq.listener.utils.IgnorableExceptions;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.classify.BinaryExceptionClassifier;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 21:02
 **/
public abstract class IgnoredExceptionListenerWrapper extends RetryDelayTimeMessageListenerWrapper {

    private static final Logger logger = LoggerFactory.getLogger(IgnoredExceptionListenerWrapper.class);

    private final BinaryExceptionClassifier classifier;

    public IgnoredExceptionListenerWrapper(Object consumerClass, Method consumerMethod, String key, int delayLevel, Class<? extends Throwable>[] ignorableExceptions) {
        super(consumerClass, consumerMethod, key, delayLevel, ignorableExceptions);
        this.classifier = new BinaryExceptionClassifier(buildIgnorableExceptions(ignorableExceptions), false);
    }

    @Override
    protected ConsumeConcurrentlyStatus retryDelayConsumerMessageWrapper(List<MessageExt> messageList, ConsumeConcurrentlyContext context) {

        try {
            // 执行下层业务逻辑
            return ignoredExceptionConsumerMessageWrapper(messageList, context);
        } catch (Exception exception) {

            // 可以被忽略的异常
            if (this.classifier.classify(exception)) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

            throw exception;
        }

    }

    private IgnorableExceptions buildIgnorableExceptions(Class<? extends Throwable>[] exceptions) {
        return new IgnorableExceptions(exceptions);
    }

    protected abstract ConsumeConcurrentlyStatus ignoredExceptionConsumerMessageWrapper(List<MessageExt> messageList, ConsumeConcurrentlyContext context);

}
