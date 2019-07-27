package me.galaxy.rocket;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;

import java.util.Map;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-24 14:03
 **/
public class RocketConsumerSpringLifecycle implements SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(RocketConsumerSpringLifecycle.class);

    @Autowired(required = false)
    private Map<String, DefaultMQPushConsumer> consumers;

    private boolean isRunning = false;

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void start() {

        if (logger.isInfoEnabled()) {
            logger.info("启动RocketMQ Consumer监听器[共{}个]", consumers == null ? 0 : consumers.size());
        }

        if (consumers == null || consumers.size() == 0) {
            return;
        }

        for (Map.Entry<String, DefaultMQPushConsumer> entry : consumers.entrySet()) {
            startConsumer(entry.getKey(), entry.getValue());
        }

        this.isRunning = true;
    }

    private void startConsumer(String name, DefaultMQPushConsumer consumer) {

        String[] infos = name.split("-");

        try {
            consumer.start();
            logger.info(String.format("启动RocketMQ Consumer监听器[%s]:%s，Topic=%s，Tag=%s - 成功", infos[1], infos[2], infos[3], infos[4]));
        } catch (MQClientException e) {
            logger.warn(String.format("启动RocketMQ Consumer监听器[%s]:%s，Topic=%s，Tag=%s - 失败", infos[1], infos[2], infos[3], infos[4]), e);
        }

    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public void stop() {

        if (logger.isInfoEnabled()) {
            logger.info("关闭RocketMQ Consumer监听器[共{}个]", consumers == null ? 0 : consumers.size());
        }

        if (consumers == null || consumers.size() == 0) {
            return;
        }

        for (Map.Entry<String, DefaultMQPushConsumer> entry : consumers.entrySet()) {
            shutdownConsumer(entry.getKey(), entry.getValue());
        }

        this.isRunning = false;
    }

    private void shutdownConsumer(String name, DefaultMQPushConsumer consumer) {
        consumer.shutdown();

        String[] infos = name.split("-");
        logger.info(String.format("关闭RocketMQ Consumer监听器[%s]:%s，Topic=%s，Tag=%s - 成功", infos[1], infos[2], infos[3], infos[4]));
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

}
