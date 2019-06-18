package me.galaxy.rocketmq.old;

import me.galaxy.rocketmq.annotation.RocketListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static me.galaxy.rocketmq.old.RocketBeanDefinitionConstant.*;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-17 15:27
 **/
public class RocketListenerAnnotationPostProcessor implements BeanDefinitionRegistryPostProcessor {

    public static final Logger logger = LoggerFactory.getLogger(RocketListenerAnnotationPostProcessor.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        String[] beanNames = registry.getBeanDefinitionNames();

        for (String beanName : beanNames) {

            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);

            Class<?> beanClass;

            try {
                beanClass = resolveBeanClass(beanDefinition, registry);
            } catch (ClassNotFoundException e) {
                throw new BeanDefinitionStoreException(String.format("解析BeanDefinition失败，找不到类：%s", beanDefinition.getBeanClassName()), e);
            }

            Map<Method, RocketListener> annotatedMethods = rocketAnnotatedMethod(beanClass);

            if (annotatedMethods.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, RocketListener> entry : annotatedMethods.entrySet()) {

                RocketListener listener = entry.getValue();
                BeanDefinition rocketConsumer = new RootBeanDefinition(DefaultMQPushConsumer.class);
                rocketConsumer.setDependsOn(rocketConsumer.getFactoryBeanName());

                MutablePropertyValues mpv = rocketConsumer.getPropertyValues();
                mpv.add(CONSUMER_GROUP, listener.consumerGroup());
                mpv.add(TOPIC, listener.topic());
                mpv.add(TAG, listener.tag());
                mpv.add(KEY, listener.key());
                mpv.add(CONSUMER_BEAN_NAME, beanName);
                mpv.add(CONSUMER_METHOD, entry.getKey());

                registry.registerBeanDefinition(beanName, rocketConsumer);

            }

        }

    }

    private Class<?> resolveBeanClass(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry) throws ClassNotFoundException {

        String beanClassName = beanDefinition.getBeanClassName();

        Class<?> beanClass = null;

        if (beanClassName != null) {
            beanClass = resolveClass(beanClassName);
        } else if (beanDefinition.getFactoryMethodName() != null) {
            String factoryMethodName = beanDefinition.getFactoryMethodName();
            String factoryBeanClassName = beanDefinitionRegistry.getBeanDefinition(beanDefinition.getFactoryBeanName()).getBeanClassName();
            Class<?> factoryBeanClass = resolveClass(factoryBeanClassName);
            Set<Method> candidateMethods = new HashSet<>();
            ReflectionUtils.doWithMethods(factoryBeanClass,
                    candidateMethods::add,
                    (method) -> method.getName().equals(factoryMethodName) && method.isAnnotationPresent(Bean.class)
            );
            Assert.state(candidateMethods.size() == 1, String.format("[%s]类中标识@Bean的方法[%s]不止一个", factoryBeanClass.getName(), factoryMethodName));
            beanClass = candidateMethods.iterator().next().getReturnType();
        }

        Assert.notNull(beanClass, String.format("解析[%s]BeanDefinition出现异常,BeanClass解析失败", beanDefinition.toString()));
        return beanClass;

    }

    private Class<?> resolveClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    private Map<Method, RocketListener> rocketAnnotatedMethod(Class<?> beanClass) {
        return MethodIntrospector.selectMethods(beanClass, new MethodIntrospector.MetadataLookup<RocketListener>() {
            @Override
            public RocketListener inspect(Method method) {
                return method.getAnnotation(RocketListener.class);
            }
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

}
