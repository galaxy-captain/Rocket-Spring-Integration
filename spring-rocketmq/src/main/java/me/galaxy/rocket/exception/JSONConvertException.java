package me.galaxy.rocket.exception;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-06-25 18:14
 **/
public class JSONConvertException extends RuntimeException {

    public JSONConvertException() {
    }

    public JSONConvertException(String message) {
        super(message);
    }

    public JSONConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONConvertException(Throwable cause) {
        super(cause);
    }

    public JSONConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
