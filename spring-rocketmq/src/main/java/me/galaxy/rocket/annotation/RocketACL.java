package me.galaxy.rocket.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface RocketACL {

    String accessKey();

    String secretKey();

}
