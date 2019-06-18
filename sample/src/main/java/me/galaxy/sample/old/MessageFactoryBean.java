package me.galaxy.sample.old;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-17 21:34
 **/
@Component
public class MessageFactoryBean implements FactoryBean<MessageListener> {

    public MessageListener getObject() throws Exception {
        return new MessageListener();
    }

    public Class<?> getObjectType() {
        return MessageListener.class;
    }

    public boolean isSingleton() {
        return false;
    }

}
