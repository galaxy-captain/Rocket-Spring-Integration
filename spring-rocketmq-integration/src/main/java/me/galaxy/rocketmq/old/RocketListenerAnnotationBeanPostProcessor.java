package me.galaxy.rocketmq.old;

import me.galaxy.rocketmq.listener.RegisterMessageListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import static me.galaxy.rocketmq.old.RocketBeanDefinitionConstant.*;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-17 21:56
 **/
public class RocketListenerAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(RocketListenerAnnotationBeanPostProcessor.class);

    private ConfigurableBeanFactory beanFactory;

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

        if (bean instanceof DefaultMQPushConsumer) {
            registerConsumer(pvs, pds, (DefaultMQPushConsumer) bean, beanName);
        }

        return super.postProcessPropertyValues(pvs, pds, bean, beanName);
    }

    private void registerConsumer(PropertyValues pvs, PropertyDescriptor[] pds, DefaultMQPushConsumer consumer, String beanName) {
        String consumerGroup = getStringProperty(pvs, CONSUMER_GROUP);
        String topic = getStringProperty(pvs, TOPIC);
        String tag = getStringProperty(pvs, TAG);
        String key = getStringProperty(pvs, KEY);

        if (StringUtils.isEmpty(consumerGroup)) {
            throw new NullPointerException(String.format("RocketMQ Consumer[%s]中consumerGroup为必填项", beanName));
        }

        if (StringUtils.isEmpty(topic)) {
            throw new NullPointerException(String.format("RocketMQ Consumer[%s]中topic为必填项", beanName));
        }

        if (StringUtils.isEmpty(tag)) {
            throw new NullPointerException(String.format("RocketMQ Consumer[%s]中tag为必填项", beanName));
        }

        consumer.setConsumerGroup(consumerGroup);

        try {
            consumer.subscribe(topic, tag);
        } catch (MQClientException e) {
            throw new RuntimeException(String.format("RocketMQ Consumer[%s]订阅(topic,tag)=[%s,%s]失败", beanName, topic, tag), e);
        }

        Object consumerClass = beanFactory.getBean(getStringProperty(pvs, CONSUMER_BEAN_NAME));
        Method consumerMethod = getProperty(pvs, CONSUMER_METHOD, Method.class);
        if (AopUtils.isAopProxy(consumerClass)) {
            consumerMethod = MethodIntrospector.selectInvocableMethod(consumerMethod, consumerClass.getClass());
        }
        consumer.registerMessageListener(new RegisterMessageListener(consumerClass, consumerMethod, key));

    }

    private String getStringProperty(PropertyValues pvs, String propertyName) {
        String value = (String) pvs.getPropertyValue(propertyName).getValue();
        if (value.startsWith("$")) {
            value = beanFactory.resolveEmbeddedValue(value);
        }
        return value;
    }

    private <T> T getProperty(PropertyValues pvs, String name, Class<T> targetClass) {
        return (T) pvs.getPropertyValue(name).getValue();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

}
