package me.galaxy.sample;

import me.galaxy.rocketmq.RocketListener;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-17 17:34
 **/
public class MessageListener {

    @RocketListener
    public void service(String msg) {
        System.out.println(msg);
    }

}
