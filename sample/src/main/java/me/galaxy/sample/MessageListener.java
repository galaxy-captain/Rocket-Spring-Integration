package me.galaxy.sample;

import me.galaxy.rocketmq.annotation.RocketListener;
import me.galaxy.rocketmq.annotation.RocketNameServer;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 13:20
 **/
@RocketNameServer
@Service
public class MessageListener {

    @RocketListener(consumerGroup = "test_consumer_group", topic = "test_topic", tag = "test_tag")
    public void service() {

    }

}
