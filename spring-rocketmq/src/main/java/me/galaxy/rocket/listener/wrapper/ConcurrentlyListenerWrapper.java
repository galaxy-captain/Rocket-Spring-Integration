package me.galaxy.rocket.listener.wrapper;

import me.galaxy.rocket.listener.AbstractMessageListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 19:08
 **/
public class ConcurrentlyListenerWrapper implements MessageListenerConcurrently {

    private AbstractMessageListener listener;

    public ConcurrentlyListenerWrapper(AbstractMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        return  this.listener.consumeMessage(msgs, context);
    }

}
