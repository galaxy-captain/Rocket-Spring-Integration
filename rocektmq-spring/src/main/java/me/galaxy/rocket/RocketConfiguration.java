package me.galaxy.rocket;

import me.galaxy.rocket.config.ExceptionIgnore;
import me.galaxy.rocket.config.NoException;
import me.galaxy.rocket.config.NoIgnore;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 15:00
 **/
public class RocketConfiguration {

    private String nameServer = "";

    private int delayTimeLevel = Integer.MIN_VALUE;

    private List<Class<? extends Throwable>> ignorableExceptions = new LinkedList<>();

    private List<Class<? extends ExceptionIgnore>> exceptionIgnores = new LinkedList<>();

    public RocketConfiguration() {
        this.exceptionIgnores.add(NoIgnore.class);
        this.ignorableExceptions.add(NoException.class);
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public int getDelayTimeLevel() {
        return delayTimeLevel;
    }

    public void setDelayTimeLevel(int delayTimeLevel) {
        this.delayTimeLevel = delayTimeLevel;
    }

    public List<Class<? extends Throwable>> getIgnorableExceptions() {
        return ignorableExceptions;
    }

    public void setIgnorableExceptions(List<Class<? extends Throwable>> ignorableExceptions) {
        this.ignorableExceptions = ignorableExceptions;
    }

    public List<Class<? extends ExceptionIgnore>> getExceptionIgnores() {
        return exceptionIgnores;
    }

    public void setExceptionIgnores(List<Class<? extends ExceptionIgnore>> exceptionIgnores) {
        this.exceptionIgnores = exceptionIgnores;
    }
}
