package me.galaxy.test;

import me.galaxy.rocket.RocketTemplate;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;

import static me.galaxy.sample.RocketMQConstant.*;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-28 23:04
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class ProducerTest {

    @Autowired
    private RocketTemplate rocketTemplate;

    @Test
    public void testProducerSendSync() {
        SendResult result = rocketTemplate.convertAndSend("producer synchronized send message", CONCURRENTLY_MESSAGE_TOPIC);
        System.out.println(result);
    }

    @Test
    public void testProducerSendASync() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(0);

        rocketTemplate.convertAndSend("producer asynchronous send message", CONCURRENTLY_MESSAGE_TOPIC, new SendCallback() {

            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult);
                latch.countDown();
            }

            public void onException(Throwable e) {
                System.out.println(e.getMessage());
            }

        });

        latch.await();

    }

}
