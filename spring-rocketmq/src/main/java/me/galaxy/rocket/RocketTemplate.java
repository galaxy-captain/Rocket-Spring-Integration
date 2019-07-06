package me.galaxy.rocket;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import static me.galaxy.rocket.utils.MessageConverter.convertObjectToByteArray;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-24 20:20
 **/
public class RocketTemplate implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RocketTemplate.class);

    private String nameServer;

    private String producerGroup;

    private String instanceName;

    private int retryTimesWhenSendFailed = -1;

    private int retryTimesWhenSendAsyncFailed = -1;

    private int sendMsgTimeout = 3000;

    private DefaultMQProducer producer = null;

    private int delayTimeLevel = 0;

    private AclClientRPCHook aclHook;

    private boolean isRunning = false;

    public RocketTemplate() {

    }

    public RocketTemplate(DefaultMQProducer producer) {
        this.producer = producer;
        this.initProperties();
    }

    public RocketTemplate(DefaultMQProducer producer, int delayTimeLevel) {
        this(producer);
        this.delayTimeLevel = delayTimeLevel;
    }

    public int getDelayTimeLevel() {
        return delayTimeLevel;
    }

    public void setDelayTimeLevel(int delayTimeLevel) {
        this.delayTimeLevel = delayTimeLevel;
    }

    // =========================== 同步发送消息 ===========================

    // 发送消息

    public SendResult convertAndSend(Object msg, String topic) {
        return convertAndSend(msg, topic, "*");
    }

    public SendResult convertAndSend(Object msg, String topic, long timeout) {
        return convertAndSend(msg, topic, "*", timeout, null, -1);
    }

    public SendResult convertAndSend(Object msg, String topic, String tags) {
        return convertAndSend(msg, topic, tags, "");
    }

    public SendResult convertAndSend(Object msg, String topic, String tags, long timeout) {
        return convertAndSend(msg, topic, tags, "", timeout, null, -1);
    }

    public SendResult convertAndSend(Object msg, String topic, String tags, String keys) {
        return convertAndSend(msg, topic, tags, keys, sendMsgTimeout, null, 0);
    }

    public SendResult convertAndSend(Object msg, String topic, String tags, String keys, long timeout) {
        return convertAndSend(msg, topic, tags, keys, timeout, null, -1);
    }

    // 顺序发送消息

    public SendResult convertAndSend(Object msg, String topic, MessageQueueSelector selector, int order) {
        return convertAndSend(msg, topic, "*", selector, order);
    }

    public SendResult convertAndSend(Object msg, String topic, long timeout, MessageQueueSelector selector, int order) {
        return convertAndSend(msg, topic, "*", timeout, selector, order);
    }

    public SendResult convertAndSend(Object msg, String topic, String tags, MessageQueueSelector selector, int order) {
        return convertAndSend(msg, topic, tags, "", selector, order);
    }

    public SendResult convertAndSend(Object msg, String topic, String tags, long timeout, MessageQueueSelector selector, int order) {
        return convertAndSend(msg, topic, tags, "", timeout, selector, order);
    }

    public SendResult convertAndSend(Object msg, String topic, String tags, String keys, MessageQueueSelector selector, int order) {
        return convertAndSend(msg, topic, tags, keys, sendMsgTimeout, selector, order);
    }

    public SendResult convertAndSend(Object msg, String topic, String tags, String keys, long timeout, MessageQueueSelector selector, int order) {

        Message message = createMessage(msg, topic, tags, keys, delayTimeLevel);

        try {
            if (selector == null) {
                // 发送消息
                return producer.send(message, timeout);
            } else {
                // 顺序发送消息
                return producer.send(message, selector, order, timeout);
            }
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    // =========================== 异步发送消息 ===========================

    // 发送消息

    public void convertAndSend(Object msg, String topic, SendCallback callback) {
        convertAndSend(msg, topic, "*", callback);
    }

    public void convertAndSend(Object msg, String topic, long timeout, SendCallback callback) {
        convertAndSend(msg, topic, "*", timeout, callback);
    }

    public void convertAndSend(Object msg, String topic, String tags, SendCallback callback) {
        convertAndSend(msg, topic, tags, "", callback);
    }

    public void convertAndSend(Object msg, String topic, String tags, long timeout, SendCallback callback) {
        convertAndSend(msg, topic, tags, "", timeout, callback);
    }

    public void convertAndSend(Object msg, String topic, String tags, String keys, SendCallback callback) {
        convertAndSend(msg, topic, tags, keys, sendMsgTimeout, callback);
    }

    public void convertAndSend(Object msg, String topic, String tags, String keys, long timeout, SendCallback callback) {

        Message message = createMessage(msg, topic, tags, keys, delayTimeLevel);

        try {
            this.producer.send(message, callback, timeout);
        } catch (MQClientException | RemotingException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 构造Message消息
     */
    public static Message createMessage(Object msg, String topic, String tags, String keys, int delayTimeLevel) {

        byte[] body = convertObjectToByteArray(msg);

        Message message = new Message();

        if (delayTimeLevel > 0) {
            message.setDelayTimeLevel(delayTimeLevel);
        }

        if (!StringUtils.isEmpty(topic)) {
            message.setTopic(topic);
        }

        if (!StringUtils.isEmpty(tags)) {
            message.setTags(tags);
        }

        if (!StringUtils.isEmpty(keys)) {
            message.setKeys(keys);
        }

        if (body.length > 0) {
            message.setBody(body);
        }

        return message;
    }

    @Override
    public synchronized void afterPropertiesSet() throws Exception {

        if (this.producer == null) {
            this.producer = buildMQProducer();
        }

        if (!isRunning) {
            this.isRunning = true;
            this.producer.start();
        }

    }

    private void initProperties() {
        this.nameServer = this.producer.getNamesrvAddr();
        this.producerGroup = this.producer.getProducerGroup();
        this.instanceName = this.producer.getInstanceName();
        this.retryTimesWhenSendAsyncFailed = this.producer.getRetryTimesWhenSendAsyncFailed();
        this.retryTimesWhenSendFailed = this.producer.getRetryTimesWhenSendFailed();
        this.sendMsgTimeout = this.producer.getSendMsgTimeout();
    }

    private DefaultMQProducer buildMQProducer() {

        DefaultMQProducer producer;

        if (aclHook == null) {
            producer = new DefaultMQProducer();
        } else {
            producer = new DefaultMQProducer(aclHook);
        }

        if (!StringUtils.isEmpty(this.nameServer)) {
            producer.setNamesrvAddr(this.nameServer);
        }

        if (!StringUtils.isEmpty(this.producerGroup)) {
            producer.setProducerGroup(this.producerGroup);
        }

        if (!StringUtils.isEmpty(this.instanceName)) {
            producer.setInstanceName(this.instanceName);
        }

        if (this.retryTimesWhenSendAsyncFailed > -1) {
            producer.setRetryTimesWhenSendAsyncFailed(this.retryTimesWhenSendAsyncFailed);
        }

        if (this.retryTimesWhenSendFailed > -1) {
            producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        }

        if (this.sendMsgTimeout > 0) {
            producer.setSendMsgTimeout(this.sendMsgTimeout);
        }

        return producer;
    }

}
