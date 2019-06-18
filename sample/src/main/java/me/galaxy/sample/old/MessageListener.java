package me.galaxy.sample.old;

import me.galaxy.rocketmq.annotation.RocketListener;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-17 17:34
 **/
public class MessageListener {

    @RocketListener(topic = "TEST_TOPIC")
    public void service(String msg) {
        System.out.println(msg);
    }

}
