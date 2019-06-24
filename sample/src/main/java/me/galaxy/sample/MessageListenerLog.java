package me.galaxy.sample;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-24 15:09
 **/
@Aspect
public class MessageListenerLog {

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerLog.class);

    @Pointcut("@annotation(me.galaxy.sample.LogAspect)")
    public void cutLogAspect() {

    }

    public MessageListenerLog() {

    }

    @Before("cutLogAspect()")
    public void before() {
        logger.info("开始消费");
    }

    @After("cutLogAspect()")
    public void after() {
        logger.info("结束消费");
    }
}
