package me.galaxy.rocket;

import me.galaxy.rocket.annotation.RocketConsumer;
import me.galaxy.rocket.annotation.RocketListener;
import me.galaxy.rocket.config.ConsumerConfig;
import me.galaxy.rocket.config.NoException;
import me.galaxy.rocket.config.NoIgnore;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringUtils;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 17:21
 **/
public class ConsumerConfigDetector {

    public static ConsumerConfig buildConsumerConfig(DefaultListableBeanFactory beanFactory,
                                                     RocketConfiguration globalConfig,
                                                     RocketConsumer classConfig,
                                                     RocketListener methodConfig) {

        ConsumerConfig configuration = new ConsumerConfig();

        setGlobalConfiguration(globalConfig, configuration, beanFactory);
        setClassConfiguration(classConfig, configuration, beanFactory);
        setMethodConfiguration(methodConfig, configuration, beanFactory);

        return configuration;

    }

    private static void setGlobalConfiguration(RocketConfiguration globalConfig, ConsumerConfig cfg, DefaultListableBeanFactory beanFactory) {

        if (globalConfig == null) {
            return;
        }

        // 解析占位符
        cfg.setNameServer(resolvePlaceholderWithProperties(beanFactory, globalConfig.getNameServer()));

        cfg.setRetryConsumeTimes(globalConfig.getRetryTimes());
        cfg.setDelayTimeLevel(globalConfig.getDelayTimeLevel());
        cfg.setSuspendTimeMillis(globalConfig.getSuspendTimeMillis());
        cfg.setIgnorableExceptions(globalConfig.getIgnorableExceptions());
        cfg.setExceptionIgnores(globalConfig.getExceptionIgnores());
        cfg.setAclHook(globalConfig.getAclHook());

    }

    private static void setClassConfiguration(RocketConsumer classConfig, ConsumerConfig cfg, DefaultListableBeanFactory beanFactory) {

        if (classConfig == null) {
            return;
        }

        String nameServer = resolvePlaceholderWithProperties(beanFactory, classConfig.nameServer());
        if (!StringUtils.isEmpty(nameServer)) {
            cfg.setNameServer(nameServer);
        }

        if (classConfig.retryTimes() > Integer.MIN_VALUE) {
            cfg.setRetryConsumeTimes(classConfig.retryTimes());
        }

        if (classConfig.delayTimeLevel() > Integer.MIN_VALUE) {
            cfg.setDelayTimeLevel(classConfig.delayTimeLevel());
        }

        if (classConfig.suspendTimeMillis() > Integer.MIN_VALUE) {
            cfg.setSuspendTimeMillis(classConfig.suspendTimeMillis());
        }

        String topic = resolvePlaceholderWithProperties(beanFactory, classConfig.topic());
        if (!StringUtils.isEmpty(topic)) {
            cfg.setTopic(topic);
        }

        if (classConfig.ignoredExceptions().length > 0) {
            cfg.setIgnorableExceptions(classConfig.ignoredExceptions());
        }

        if (classConfig.exceptionIgnores().length > 0) {
            cfg.setExceptionIgnores(classConfig.exceptionIgnores());
        }

    }

    private static void setMethodConfiguration(RocketListener methodConfig, ConsumerConfig cfg, DefaultListableBeanFactory beanFactory) {

        if (methodConfig == null) {
            return;
        }

        cfg.setOrderly(methodConfig.orderly());

        String nameServer = resolvePlaceholderWithProperties(beanFactory, methodConfig.nameServer());
        if (!StringUtils.isEmpty(nameServer)) {
            cfg.setNameServer(nameServer);
        }

        String instanceName = resolvePlaceholderWithProperties(beanFactory, methodConfig.instance());
        if (!StringUtils.isEmpty(instanceName)) {
            cfg.setInstance(instanceName);
        }

        String consumerGroup = resolvePlaceholderWithProperties(beanFactory, methodConfig.consumerGroup());
        cfg.setConsumerGroup(consumerGroup);

        String topic = resolvePlaceholderWithProperties(beanFactory, methodConfig.topic());
        if (!StringUtils.isEmpty(topic)) {
            cfg.setTopic(topic);
        }

        String tag = resolvePlaceholderWithProperties(beanFactory, methodConfig.tag());
        if (!StringUtils.isEmpty(tag)) {
            cfg.setTag(tag);
        }

        if (methodConfig.maxBatchSize() > 0) {
            cfg.setMaxBatchSize(methodConfig.maxBatchSize());
        }

        if (methodConfig.retryTimes() > Integer.MIN_VALUE) {
            cfg.setRetryConsumeTimes(methodConfig.retryTimes());
        }

        if (methodConfig.delayTimeLevel() > Integer.MIN_VALUE) {
            cfg.setDelayTimeLevel(methodConfig.delayTimeLevel());
        }

        if (methodConfig.suspendTimeMillis() > Integer.MIN_VALUE) {
            cfg.setSuspendTimeMillis(methodConfig.suspendTimeMillis());
        }

        if (methodConfig.ignoredExceptions().length > 0) {
            cfg.setIgnorableExceptions(methodConfig.ignoredExceptions());
        }

        if (methodConfig.exceptionIgnores().length > 0) {
            cfg.setExceptionIgnores(methodConfig.exceptionIgnores());
        }

        cfg.setHook(methodConfig.hook());

    }

    /**
     * 如果是${...}占位符，则解析数据并返回，否则不做处理
     *
     * @param beanFactory
     * @param value
     * @return
     */
    public static String resolvePlaceholderWithProperties(DefaultListableBeanFactory beanFactory, String value) {
        return isPlaceholderValue(value) ? beanFactory.resolveEmbeddedValue(value) : value;
    }

    public static boolean isPlaceholderValue(String value) {
        return value.startsWith("${") && value.endsWith("}");
    }

}
