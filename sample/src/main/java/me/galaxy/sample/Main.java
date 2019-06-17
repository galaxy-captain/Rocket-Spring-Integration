package me.galaxy.sample;

import me.galaxy.rocketmq.EnableRocket;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-06-17 20:52
 **/
@EnableRocket
public class Main {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        MessageListener messageListener = context.getBean("messageListener1", MessageListener.class);
        messageListener.service("aaaa");

    }

}
