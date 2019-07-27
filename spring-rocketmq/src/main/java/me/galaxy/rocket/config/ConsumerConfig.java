package me.galaxy.rocket.config;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
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

    public static final boolean DEFAULT_ORDERLY = false;

    public static final String DEFAULT_NAME_SERVER = "127.0.0.1:9876";

    public static final String DEFAULT_TAG = "*";

    private String simpleName;

    private boolean orderly = DEFAULT_ORDERLY;

    private String nameServer = DEFAULT_NAME_SERVER;

    private String instance;

    private String consumerGroup;

    private String topic;

    private String tag = DEFAULT_TAG;

    private int maxBatchSize;

    private int retryConsumeTimes;

    private int delayTimeLevel;

    private long suspendTimeMillis;

    private Class<? extends Throwable>[] ignorableExceptions = new Class[0];

    private Class<? extends ExceptionIgnore>[] exceptionIgnores = new Class[0];

    private Class<? extends RPCHook>[] hook = new Class[0];

    private AclClientRPCHook aclHook;

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

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

    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    public void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }

    public int getRetryConsumeTimes() {
        return retryConsumeTimes;
    }

    public void setRetryConsumeTimes(int retryConsumeTimes) {
        this.retryConsumeTimes = retryConsumeTimes;
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

    public Class<? extends RPCHook>[] getHook() {
        return hook;
    }

    public void setHook(Class<? extends RPCHook>[] hook) {
        this.hook = hook;
    }

    public AclClientRPCHook getAclHook() {
        return aclHook;
    }

    public void setAclHook(AclClientRPCHook aclHook) {
        this.aclHook = aclHook;
    }
}
