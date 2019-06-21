package me.galaxy.rocket.listener.wrapper;

import me.galaxy.rocket.listener.AbstractMessageListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 19:07
 **/
public class OrderlyListenerWrapper implements MessageListenerOrderly {

    private AbstractMessageListener listener;

    public OrderlyListenerWrapper(AbstractMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        return this.listener.consumeMessage(msgs, context);
    }

}
