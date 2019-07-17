package me.galaxy.rocket.config;

import com.sun.org.apache.bcel.internal.generic.FADD;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-07-17 13:45
 **/
public class InfoConfig {

    private static final String INIT_TEMPLATE = "RocketMQ Initializing - %s";

    private static final String START_TEMPLATE = "RocketMQ Starting - %s";

    private static final String STOP_TEMPLATE = "RocketMQ Stopping - %s";

    public static final String SHOW_TEMPLATE = "RocketMQ - %s";

    private static final String CONSUMER_TEMPLATE = "%s Consumer[topic=%s,tag=%s] %s";

    private static final String START = "START";

    public static final String STOP = "STOP";

    private static final String SUCCEED = "SUCCEED";

    private static final String FAILED = "FAILURE";

    public static final String CREATE = "CREATE";

    private static String init(String args) {
        return String.format(INIT_TEMPLATE, args);
    }

    private static String initConsumer(String action, String topic, String tag, String state) {
        return init(String.format(CONSUMER_TEMPLATE, action, topic, tag, state));
    }

    public static String initConsumerSucceed(String topic, String tag) {
        return initConsumer(CREATE, topic, tag, SUCCEED);
    }

    public static String initConsumerFailed(String topic, String tag) {
        return initConsumer(CREATE, topic, tag, FAILED);
    }

    private static String start(String args) {
        return String.format(START_TEMPLATE, args);
    }

    public static String startConsumerSucceed(String topic, String tag) {
        return start(String.format(CONSUMER_TEMPLATE, START, topic, tag, SUCCEED));
    }

    public static String startConsumerFailed(String topic, String tag) {
        return start(String.format(CONSUMER_TEMPLATE, START, topic, tag, FAILED));
    }

    private static String stop(String args) {
        return String.format(STOP_TEMPLATE, args);
    }

    public static String stopConsumerSucceed(String topic, String tag) {
        return stop(String.format(CONSUMER_TEMPLATE, STOP, topic, tag, SUCCEED));
    }

    public static String stopConsumerFailed(String topic, String tag) {
        return stop(String.format(CONSUMER_TEMPLATE, STOP, topic, tag, FAILED));
    }

    public static String initConsumerShow(String args) {
        return String.format(SHOW_TEMPLATE, args);
    }

    public static String showConsumerCount(int count) {
        return initConsumerShow(String.format("Consumer Count=%s", String.valueOf(count)));
    }

}
