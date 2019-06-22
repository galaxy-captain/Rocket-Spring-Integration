package me.galaxy.rocket.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MessageConverter {

    public static String convertObjectToJSON(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T convertJSONToObject(String json, Class<T> clazz) {
        return  JSONObject.parseObject(json, clazz);
    }

}
