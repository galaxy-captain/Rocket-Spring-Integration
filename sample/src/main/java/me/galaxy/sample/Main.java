package me.galaxy.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-17 20:52
 **/
public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

//        RegisterBean registerBean = (RegisterBean) context.getBean("testRegisterBean");
//        System.out.println(registerBean.test);

//        MessageListener messageListener = context.getBean(MessageListener.class);
//        messageListener.service("test message");
//        System.out.println(context.getClass().getSimpleName());

    }

}
