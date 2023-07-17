/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Lambdas
 *
 * @author tabuyos
 * @since 2023/7/17
 */
public class Lambdas {

  private static final String GET_PREFIX = "get";

  public static <T> String getName(SFunction<T, ?> fn) {
    SerializedLambda serializedLambda = getSerializedLambda(fn);
    String implMethodName = serializedLambda.getImplMethodName();
    if (implMethodName.startsWith(GET_PREFIX)) {
      implMethodName = implMethodName.substring(GET_PREFIX.length());
    }
    return implMethodName.replaceFirst(implMethodName.charAt(0) + "", (implMethodName.charAt(0) + "").toLowerCase());
  }


  private static <T> SerializedLambda getSerializedLambda(SFunction<T, ?> fn) {
    // 从function取出序列化方法
    Method writeReplaceMethod;
    try {
      writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    // 从序列化方法取出序列化的lambda信息
    boolean isAccessible = writeReplaceMethod.canAccess(fn);
    writeReplaceMethod.setAccessible(true);
    SerializedLambda serializedLambda;
    try {
      serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
    writeReplaceMethod.setAccessible(isAccessible);
    return serializedLambda;
  }
}
