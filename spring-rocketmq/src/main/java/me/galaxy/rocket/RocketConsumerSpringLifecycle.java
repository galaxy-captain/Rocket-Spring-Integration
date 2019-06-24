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

    @Autowired
    private Map<String, DefaultMQPushConsumer> consumers;

    private boolean isRunning = false;

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void start() {

        for (Map.Entry<String, DefaultMQPushConsumer> entry : consumers.entrySet()) {
            startConsumer(entry.getKey(), entry.getValue());
        }

        this.isRunning = true;
    }

    private void startConsumer(String name, DefaultMQPushConsumer consumer) {

        try {
            consumer.start();
        } catch (MQClientException e) {
            String[] infos = name.split("-");
            String em = String.format("启动[%s]:%s，Topic=%s，Tag=%s的Consumer失败", infos[1], infos[2], infos[3], infos[4]);
            logger.error(em, e);
        }

    }

    @Override
    public void stop(Runnable callback) {

    }

    @Override
    public void stop() {

        for (Map.Entry<String, DefaultMQPushConsumer> entry : consumers.entrySet()) {
            shutdownConsumer(entry.getValue());
        }

        this.isRunning = false;
    }

    private void shutdownConsumer(DefaultMQPushConsumer consumer) {
        consumer.shutdown();
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
