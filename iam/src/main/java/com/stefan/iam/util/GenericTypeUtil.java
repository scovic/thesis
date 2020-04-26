package com.stefan.iam.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericTypeUtil {
  public static Type getType(Class<?> rawClass, Class<?> parameter) {
    return new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {parameter};
      }
      @Override
      public Type getRawType() {
        return rawClass;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
    };
  }
}
