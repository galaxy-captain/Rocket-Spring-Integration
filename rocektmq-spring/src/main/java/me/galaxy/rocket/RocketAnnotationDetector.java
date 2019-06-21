package me.galaxy.rocket;

import me.galaxy.rocket.annotation.RocketListener;
import me.galaxy.rocket.annotation.RocketMQ;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.MethodIntrospector;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 14:09
 **/
public class RocketAnnotationDetector {

    public static Map<Method, RocketListener> rocketListenerAnnotatedMethod(Class<?> beanClass) {

        return MethodIntrospector.selectMethods(beanClass, new MethodIntrospector.MetadataLookup<RocketListener>() {
            @Override
            public RocketListener inspect(Method method) {
                return method.getAnnotation(RocketListener.class);
            }
        });

    }

    public static RocketMQ getRocketMQAnnotation(Object object) {

        if (object == null) {
            return null;
        }

        return object.getClass().getAnnotation(RocketMQ.class);
    }

    public static RocketConfiguration getRocketConfiguration(BeanFactory beanFactory) {

        RocketConfiguration configuration = null;

        try {
            configuration = beanFactory.getBean(RocketConfiguration.class);
        } catch (BeansException e) {
            // do nothing
        }

        return configuration;

    }

}
