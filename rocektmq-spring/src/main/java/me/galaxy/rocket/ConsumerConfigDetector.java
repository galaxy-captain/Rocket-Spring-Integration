package me.galaxy.rocket;

import me.galaxy.rocket.annotation.RocketListener;
import me.galaxy.rocket.annotation.RocketMQ;
import me.galaxy.rocket.config.ConsumerConfig;
import me.galaxy.rocket.config.NoException;
import me.galaxy.rocket.config.NoIgnore;
import org.springframework.util.StringUtils;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 17:21
 **/
public class ConsumerConfigDetector {

    public static ConsumerConfig buildConsumerConfig(RocketConfiguration globalConfig,
                                                     RocketMQ classConfig,
                                                     RocketListener methodConfig) {

        ConsumerConfig configuration = new ConsumerConfig();
        setGlobalConfiguration(globalConfig, configuration);
        setClassConfiguration(classConfig, configuration);
        setMethodConfiguration(methodConfig, configuration);

        return configuration;

    }

    private static void setGlobalConfiguration(RocketConfiguration globalConfig, ConsumerConfig cfg) {
        cfg.setNameServer(globalConfig.getNameServer());
        cfg.setDelayTimeLevel(globalConfig.getDelayTimeLevel());
        cfg.setIgnorableExceptions(globalConfig.getIgnorableExceptions());
        cfg.setExceptionIgnores(globalConfig.getExceptionIgnores());
    }

    private static void setClassConfiguration(RocketMQ classConfig, ConsumerConfig cfg) {

        if (!StringUtils.isEmpty(classConfig.nameServer())) {
            cfg.setNameServer(classConfig.nameServer());
        }

        if (classConfig.delayTimeLevel() > Integer.MIN_VALUE) {
            cfg.setDelayTimeLevel(classConfig.delayTimeLevel());
        }

        if (!StringUtils.isEmpty(classConfig.topic())) {
            cfg.setTopic(classConfig.topic());
        }

        if (!(classConfig.ignoredExceptions().length == 1 && classConfig.ignoredExceptions()[0] == NoException.class)) {
            cfg.setIgnorableExceptions(classConfig.ignoredExceptions());
        }

        if (!(classConfig.exceptionIgnores().length == 1 && classConfig.exceptionIgnores()[0] == NoIgnore.class)) {
            cfg.setExceptionIgnores(classConfig.exceptionIgnores());
        }

    }

    private static void setMethodConfiguration(RocketListener methodConfig, ConsumerConfig cfg) {

        cfg.setOrderly(methodConfig.orderly());

        if (!StringUtils.isEmpty(methodConfig.nameServer())) {
            cfg.setNameServer(methodConfig.nameServer());
        }

        if (!StringUtils.isEmpty(methodConfig.instance())) {
            cfg.setInstance(methodConfig.instance());
        }

        cfg.setConsumerGroup(methodConfig.consumerGroup());

        if (!StringUtils.isEmpty(methodConfig.topic())) {
            cfg.setTopic(methodConfig.topic());
        }

        cfg.setTag(methodConfig.tag());

        if (methodConfig.delayTimeLevel() > Integer.MIN_VALUE) {
            cfg.setDelayTimeLevel(methodConfig.delayTimeLevel());
        }

        if (!(methodConfig.ignoredExceptions().length == 1 && methodConfig.ignoredExceptions()[0] == NoException.class)) {
            cfg.setIgnorableExceptions(methodConfig.ignoredExceptions());
        }

        if (!(methodConfig.exceptionIgnores().length == 1 && methodConfig.exceptionIgnores()[0] == NoIgnore.class)) {
            cfg.setExceptionIgnores(methodConfig.exceptionIgnores());
        }

        cfg.setHook(methodConfig.hook());

    }

}
