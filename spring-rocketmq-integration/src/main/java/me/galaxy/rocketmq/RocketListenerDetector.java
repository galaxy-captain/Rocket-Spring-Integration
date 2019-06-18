package me.galaxy.rocketmq;

import me.galaxy.rocketmq.annotation.RocketListener;
import me.galaxy.rocketmq.annotation.RocketNameServer;
import me.galaxy.rocketmq.listener.RegisterMessageListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 13:24
 **/
public class RocketListenerDetector implements BeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        // create rocketmq consumer with {@code RocketListener} annotation
        this.dynamicCreateRocketConsumer(bean, beanName);

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // do nothings
        return bean;
    }

    /**
     * @param bean
     * @param beanName
     */
    private void dynamicCreateRocketConsumer(Object bean, String beanName) {

        Map<Method, RocketListener> annotatedMethods = rocketListenerAnnotatedMethod(bean.getClass());

        if (annotatedMethods.isEmpty()) return;

        RocketConfiguration rc = null;
        try {
            rc = beanFactory.getBean(RocketConfiguration.class);
        } catch (BeansException e) {
            // do nothing
        }

        for (Map.Entry<Method, RocketListener> entry : annotatedMethods.entrySet()) {
            Method method = entry.getKey();
            RocketListener listener = entry.getValue();

            String instance = listener.instance();
            String consumerGroup = listener.consumerGroup();
            String topic = listener.topic();
            String tag = listener.tag();
            String key = listener.key();

            if (StringUtils.isEmpty(consumerGroup)) throw new NullPointerException("RocketMQ Consumer中consumerGroup不能为空");
            if (StringUtils.isEmpty(topic)) throw new NullPointerException("RocketMQ Consumer中topic不能为空");
            if (StringUtils.isEmpty(tag)) tag = "*";
            if (StringUtils.isEmpty(key)) key = "*";

            String consumerName = buildConsumerName(beanName, method.getName(), instance, consumerGroup, topic, tag, key);
            DefaultMQPushConsumer consumer = buildConsumerWithReflect(bean, method, instance, consumerGroup, topic, tag, key);

            // 设置 Name Server 地址
            setConsumerNameServer(rc, bean, listener, consumer);

            registerRocketMQConsumer(consumerName, consumer);
        }

    }

    private void setConsumerNameServer(RocketConfiguration rc, Object clazz, RocketListener methodAnnotation, DefaultMQPushConsumer consumer) {

        String nameServer = null;

        if (rc != null && !StringUtils.isEmpty(rc.getNameServer()))
            nameServer = rc.getNameServer();

        RocketNameServer clazzAnnotation = clazz.getClass().getAnnotation(RocketNameServer.class);
        if (clazzAnnotation != null && !StringUtils.isEmpty(clazzAnnotation.value())) {
            nameServer = clazzAnnotation.value();
        }

        if (clazzAnnotation != null && !StringUtils.isEmpty(clazzAnnotation.address())) {
            nameServer = clazzAnnotation.address();
        }

        if (!StringUtils.isEmpty(methodAnnotation.nameServer())) {
            nameServer = methodAnnotation.nameServer();
        }

        if (StringUtils.isEmpty(nameServer)) {
            throw new NullPointerException("RocketMQ Consumer没有配置NameServer地址");
        }

        consumer.setNamesrvAddr(nameServer);
    }

    /**
     * @param beanName
     * @param methodName
     * @param topic
     * @param tag
     * @param key
     * @return
     */
    private String buildConsumerName(String beanName, String methodName, String instance, String consumerGroup, String topic, String tag, String key) {

        if (StringUtils.isEmpty(instance)) {
            return String.format("%s.%s:%s:%s-%s-%s", beanName, methodName, consumerGroup, topic, tag, key);
        }

        return String.format("%s.%s:%s-%s:%s-%s-%s", beanName, methodName, consumerGroup, instance, topic, tag, key);

    }

    /**
     * @param object
     * @param method
     * @param consumerGroup
     * @param topic
     * @param tag
     * @param key
     * @return
     */
    private DefaultMQPushConsumer buildConsumerWithReflect(Object object, Method method, String instance, String consumerGroup, String topic, String tag, String key) {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);

        try {
            consumer.subscribe(topic, tag);
        } catch (MQClientException e) {
            throw new RuntimeException(String.format("RocketMQ Consumer订阅(topic,tag)=[%s,%s]失败", topic, tag), e);
        }

        RegisterMessageListener registerMessageListener = new RegisterMessageListener(object, method, key);

        consumer.setMessageListener(registerMessageListener);

        return consumer;
    }

    private void registerRocketMQConsumer(String beanName, DefaultMQPushConsumer consumer) {

        if (this.beanFactory == null)
            throw new NullPointerException("DefaultListableBeanFactory is null");

        this.beanFactory.registerSingleton(beanName, consumer);
    }

    private Map<Method, RocketListener> rocketListenerAnnotatedMethod(Class<?> beanClass) {
        return MethodIntrospector.selectMethods(beanClass, new MethodIntrospector.MetadataLookup<RocketListener>() {
            @Override
            public RocketListener inspect(Method method) {
                return method.getAnnotation(RocketListener.class);
            }
        });
    }

}
