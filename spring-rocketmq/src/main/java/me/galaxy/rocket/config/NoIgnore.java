package me.galaxy.rocket.config;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-21 18:02
 **/
public class NoIgnore implements ExceptionIgnore {

    @Override
    public boolean ignorable(Exception e) {
        return false;
    }

}
