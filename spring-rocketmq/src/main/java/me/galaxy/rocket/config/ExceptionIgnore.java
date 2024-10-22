package me.galaxy.rocket.config;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 17:10
 **/
public interface ExceptionIgnore {

    /**
     * @param throwable 异常
     * @return {@code true} 忽略异常 {@code false} 其他
     */
    boolean ignorable(Throwable throwable);

}
