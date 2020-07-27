package com.example.notificationsservice.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JsonUtil {
  public static String toJson(Object obj) {
    Gson gson = new GsonBuilder().create();
    return gson.toJson(obj);
  }

  public static Object fromJson(String json, Class<?> c) {
    Gson gson = new Gson();
    return gson.fromJson(json, c);
  }

  public static Object fromJson(String json, Type t) {
    Gson gson = new Gson();
    return gson.fromJson(json, t);
  }
}
