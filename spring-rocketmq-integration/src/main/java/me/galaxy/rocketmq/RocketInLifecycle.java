package me.galaxy.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 15:26
 **/
public class RocketInLifecycle implements SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(RocketInLifecycle.class);

    @Autowired
    private Map<String, DefaultMQPushConsumer> consumers;

    private boolean isRunning = false;

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void start() {

        System.out.println("start");

        for (Map.Entry<String, DefaultMQPushConsumer> entry : consumers.entrySet()) {

            String name = entry.getKey();
            DefaultMQPushConsumer consumer = entry.getValue();

            try {
                consumer.start();
            } catch (MQClientException e) {
                logger.error("RocketMQ Consumer[{}]启动失败", name, e);
            }
        }

        isRunning = true;

    }

    @Override
    public void stop() {
        System.out.println("stop");
    }

    @Override
    public void stop(Runnable callback) {
        System.out.println("stop-callback");
    }

    @PreDestroy
    public void destroy() {

        System.out.println("destroy");

        for (Map.Entry<String, DefaultMQPushConsumer> entry : consumers.entrySet()) {
            DefaultMQPushConsumer consumer = entry.getValue();
            consumer.shutdown();
        }

        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

}
