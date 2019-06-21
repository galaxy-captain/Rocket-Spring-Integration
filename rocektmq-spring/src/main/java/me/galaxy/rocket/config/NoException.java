package me.galaxy.rocket.config;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 14:13
 **/
public class NoException extends Exception {

    public NoException() {
        super("Used in RocketMQ ignorable exception filter.");
    }
}
