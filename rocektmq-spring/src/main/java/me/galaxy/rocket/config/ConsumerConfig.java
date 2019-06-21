package me.galaxy.rocket.config;

import org.apache.rocketmq.remoting.RPCHook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 17:06
 **/
public class ConsumerConfig {

    private boolean orderly;

    private String nameServer;

    private String instance;

    private String consumerGroup;

    private String topic;

    private String tag;

    private int delayTimeLevel;

    private long suspendTimeMillis;

    private Class<? extends Throwable>[] ignorableExceptions;

    private Class<? extends ExceptionIgnore>[] exceptionIgnores;

    private Class<? extends RPCHook> hook;

    public boolean isOrderly() {
        return orderly;
    }

    public void setOrderly(boolean orderly) {
        this.orderly = orderly;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getDelayTimeLevel() {
        return delayTimeLevel;
    }

    public void setDelayTimeLevel(int delayTimeLevel) {
        this.delayTimeLevel = delayTimeLevel;
    }

    public long getSuspendTimeMillis() {
        return suspendTimeMillis;
    }

    public void setSuspendTimeMillis(long suspendTimeMillis) {
        this.suspendTimeMillis = suspendTimeMillis;
    }

    public Class<? extends Throwable>[] getIgnorableExceptions() {
        return ignorableExceptions;
    }

    public void setIgnorableExceptions(Class<? extends Throwable>[] ignorableExceptions) {
        this.ignorableExceptions = ignorableExceptions;
    }

    public Class<? extends ExceptionIgnore>[] getExceptionIgnores() {
        return exceptionIgnores;
    }

    public void setExceptionIgnores(Class<? extends ExceptionIgnore>[] exceptionIgnores) {
        this.exceptionIgnores = exceptionIgnores;
    }

    public Class<? extends RPCHook> getHook() {
        return hook;
    }

    public void setHook(Class<? extends RPCHook> hook) {
        this.hook = hook;
    }
}
