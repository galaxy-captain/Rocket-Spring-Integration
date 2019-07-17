package me.galaxy.sample;

import me.galaxy.rocket.RocketTemplate;
import me.galaxy.rocket.utils.MessageConverter;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-17 20:52
 **/
public class Main implements SendCallback {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        RocketTemplate rocketTemplate = context.getBean(RocketTemplate.class);

        SendResult result = rocketTemplate.convertAndSend(new SimpleMessage(1), "topic_test_2", "tag_test_2");

        Thread.sleep(5000);

        context.stop();

        rocketTemplate.convertAndSend(new SimpleMessage(1), "topic_test_2", "tag_test_2", new Main());
        rocketTemplate.convertAndSend(new SimpleMessage(2), "topic_test_2", "tag_test_2", new Main());
        rocketTemplate.convertAndSend(new SimpleMessage(3), "topic_test_2", "tag_test_2", new Main());
        rocketTemplate.convertAndSend(new SimpleMessage(4), "topic_test_2", "tag_test_2", new Main());
        rocketTemplate.convertAndSend(new SimpleMessage(5), "topic_test_2", "tag_test_2", new Main());
        rocketTemplate.convertAndSend(new SimpleMessage(6), "topic_test_2", "tag_test_2", new Main());
        rocketTemplate.convertAndSend(new SimpleMessage(7), "topic_test_2", "tag_test_2", new Main());



        rocketTemplate.convertAndSend("hello world1", "topic_test_2", "tag_test_1", new Main());
        rocketTemplate.convertAndSend("hello world2", "topic_test_2", "tag_test_1", new Main());
        rocketTemplate.convertAndSend("hello world3", "topic_test_2", "tag_test_1", new Main());
        rocketTemplate.convertAndSend("hello world4", "topic_test_2", "tag_test_1", new Main());
        rocketTemplate.convertAndSend("hello world5", "topic_test_2", "tag_test_1", new Main());
        rocketTemplate.convertAndSend("hello world6", "topic_test_2", "tag_test_1", new Main());
        rocketTemplate.convertAndSend("hello world7", "topic_test_2", "tag_test_1", new Main());
        rocketTemplate.convertAndSend("hello world8", "topic_test_2", "tag_test_1", new Main());
        rocketTemplate.convertAndSend("hello world9", "topic_test_2", "tag_test_1", new Main());
        rocketTemplate.convertAndSend("hello world10", "topic_test_2", "tag_test_1", new Main());


    }

    public void onSuccess(SendResult sendResult) {

    }

    public void onException(Throwable e) {

    }

}
