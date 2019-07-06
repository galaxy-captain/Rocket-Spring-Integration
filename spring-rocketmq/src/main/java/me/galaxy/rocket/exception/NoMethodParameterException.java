package me.galaxy.rocket.exception;

import org.springframework.beans.BeansException;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-23 01:38
 **/
public class NoMethodParameterException extends BeansException {

    public NoMethodParameterException(String message) {
        super(message);
    }

}
