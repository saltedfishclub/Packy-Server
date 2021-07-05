/*
 * MIT License
 *
 * Copyright (c) 2021 SaltedFish Club
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cc.sfclub.packy.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.Map;

/**
 * @author TODAY
 * @date 2020/8/31 14:59
 */
public abstract class ObjectUtils {

  private static ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
  }

  // JSON

  public static ObjectMapper getSharedMapper() {
    return objectMapper;
  }

  public static void setSharedMapper(ObjectMapper objectMapper) {
    ObjectUtils.objectMapper = objectMapper;
  }

  /**
   * javaBean、列表数组转换为json字符串
   */
  public static String toJSON(Object obj) throws IOException {
    return objectMapper.writeValueAsString(obj);
  }

  /**
   * json 转JavaBean
   */
  public static <T> T fromJSON(String jsonString, Class<T> clazz) throws IOException {
    return objectMapper.readValue(jsonString, clazz);
  }

  /**
   * json字符串转换为map
   */
  public static Map<String, Object> toMap(String jsonString) throws IOException {
    return objectMapper.readValue(jsonString, Map.class);
  }

  //
  public static boolean isEmpty(Object[] arr) {
    return arr == null || arr.length == 0;
  }

  public static boolean isNotEmpty(Object[] arr) {
    return !isEmpty(arr);
  }
}
