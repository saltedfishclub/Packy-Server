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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author TODAY
 * @date 2020/9/10 11:05
 */
public abstract class Query<T> {

  public static <T> QueryWrapper<T> create() {
    return new QueryWrapper<>();
  }

  public static <T> QueryWrapper<T> create(T entity) {
    return new QueryWrapper<>(entity);
  }

  public static <T> QueryWrapper<T> orderByAsc(String column) {
    return new QueryWrapper<T>().orderByAsc(column);
  }

  public static <T> QueryWrapper<T> orderByDesc(String column) {
    return new QueryWrapper<T>().orderByDesc(column);
  }

  public static <T> QueryWrapper<T> eq(String column, Object val) {
    return new QueryWrapper<T>().eq(column, val);
  }

  public static <T> QueryWrapper<T> eq(boolean condition, String column, Object val) {
    return new QueryWrapper<T>().eq(condition, column, val);
  }

  public static <T> QueryWrapper<T> like(String column, Object val) {
    return new QueryWrapper<T>().like(column, val);
  }

  public static <T> QueryWrapper<T> like(boolean condition, String column, Object val) {
    return new QueryWrapper<T>().like(condition, column, val);
  }

  public static <T> QueryWrapper<T> select(String... columns) {
    return new QueryWrapper<T>().select(columns);
  }

  public static <T> QueryWrapper<T> in(String column, Object... values) {
    return new QueryWrapper<T>().in(column, values);
  }
}


