package me.galaxy.rocket;

import me.galaxy.rocket.annotation.RocketListener;
import me.galaxy.rocket.annotation.RocketMQ;
import me.galaxy.rocket.config.ConsumerConfig;
import me.galaxy.rocket.config.NoRPCHook;
import me.galaxy.rocket.listener.AbstractMessageListener;
import me.galaxy.rocket.listener.wrapper.ConcurrentlyListenerWrapper;
import me.galaxy.rocket.listener.wrapper.OrderlyListenerWrapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Map;

import static me.galaxy.rocket.ConsumerConfigDetector.buildConsumerConfig;
import static me.galaxy.rocket.RocketAnnotationDetector.getRocketMQAnnotation;
import static me.galaxy.rocket.RocketAnnotationDetector.rocketListenerAnnotatedMethod;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 12:08
 **/
public class RocketAnnotationBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(RocketAnnotationBeanPostProcessor.class);

    private DefaultListableBeanFactory beanFactory;

    private boolean hasInitConfiguration = false;

    private RocketConfiguration configuration;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        // 根据注解构建Consumer监听器
        this.buildRocketMQConsumerWithAnnotation(this.beanFactory, bean, beanName);

        return bean;
    }

    @Deprecated
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // do nothing
        return bean;
    }

    /**
     * 获取全局配置
     */
    private RocketConfiguration getRocketConfiguration() {

        if (!this.hasInitConfiguration) {
            synchronized (this) {
                if (!this.hasInitConfiguration) {
                    this.configuration = RocketAnnotationDetector.getRocketConfiguration(this.beanFactory);
                    hasInitConfiguration = true;
                }
            }
        }

        return this.configuration;
    }

    private void buildRocketMQConsumerWithAnnotation(DefaultListableBeanFactory beanFactory, Object bean, String beanName) {

        // 获取全局配置类
        RocketConfiguration configuration = getRocketConfiguration();

        // 根据@RocketListener生成Consumer
        buildConsumerWithRocketListenerAnnotation(beanFactory, bean, beanName, configuration);

    }

    private void buildConsumerWithRocketListenerAnnotation(DefaultListableBeanFactory beanFactory, Object bean, String beanName, RocketConfiguration configuration) {

        Map<Method, RocketListener> annotatedMethods = rocketListenerAnnotatedMethod(bean.getClass());

        // 没有找到需要解析的注解
        if (annotatedMethods.isEmpty())
            return;

        RocketMQ rocketMQ = getRocketMQAnnotation(bean);

        for (Map.Entry<Method, RocketListener> entry : annotatedMethods.entrySet()) {

            Method method = getInvocableMethod(bean, entry.getKey());
            RocketListener rocketListener = entry.getValue();

            ConsumerConfig config = buildConsumerConfig(configuration, rocketMQ, rocketListener);

            buildRocketPushConsumer(config, bean, method);

        }

    }

    private void buildRocketPushConsumer(ConsumerConfig cfg, Object object, Method method) {

        AbstractMessageListener listener = null;

        try {

            DefaultMQPushConsumer consumer;

            if (cfg.getHook() == NoRPCHook.class) {
                consumer = new DefaultMQPushConsumer();
            } else {
                consumer = new DefaultMQPushConsumer(cfg.getHook().newInstance());
            }

            consumer.setNamesrvAddr(cfg.getNameServer());
            consumer.setInstanceName(cfg.getInstance());

            if (cfg.isOrderly()) {
                consumer.setMessageListener(new OrderlyListenerWrapper(listener));
            } else {
                consumer.setMessageListener(new ConcurrentlyListenerWrapper(listener));
            }
            consumer.subscribe(cfg.getTopic(), cfg.getTag());

        } catch (MQClientException e) {

            String msg = String.format(
                    "创建RocketMQ Consumer过程中，执行subscribe(topic,tag)失败，NameServer=%s，ConsumerGroup=%s，Topic=%s，Tag=%s",
                    cfg.getNameServer(),
                    cfg.getConsumerGroup(),
                    cfg.getTopic(),
                    cfg.getTag()
            );

            logger.error(msg, e);
        } catch (IllegalAccessException | InstantiationException e) {

            String msg = String.format(
                    "创建RocketMQ Consumer过程中，初始化RPCHook失败，NameServer=%s，ConsumerGroup=%s，Topic=%s，Tag=%s",
                    cfg.getNameServer(),
                    cfg.getConsumerGroup(),
                    cfg.getTopic(),
                    cfg.getTag()
            );

            logger.error(msg, e);
        }

    }

    /**
     * 获取Method
     *
     * @param object
     * @param method
     * @return
     */
    private Method getInvocableMethod(Object object, Method method) {

        if (object == null || method == null) return null;

        if (ClassUtils.isCglibProxy(object)) {
            method = MethodIntrospector.selectInvocableMethod(method, object.getClass());
        }

        return method;
    }

}
