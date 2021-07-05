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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cc.sfclub.packy.Constant;
import cc.sfclub.packy.ex.ApplicationException;

public abstract class DateUtils implements Constant {

  public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
  public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);
  public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

  public static String getCurrentDateTimeString() {
    return dateTimeFormatter.format(LocalDateTime.now());
  }

  /**
   * 获取指定格式的日期
   *
   * @param format
   *         指定格式 例如：“yyyy-MM-dd HH:mm:ss”
   *
   * @return 返回指定格式的日期字符串
   */
  public static String getCurrentDateTimeString(String format) {
    return DateTimeFormatter.ofPattern(format).format(LocalDateTime.now());
  }

  public static boolean hasPeriod(final LocalDate from, final LocalDate to) {
    if (from != null && to != null) {
      //检查时间是否合法
      if (to.compareTo(from) < 0) {
        throw ApplicationException.failed("起始日期应该小于终止日期");
      }
      else {
        return true;
      }
    }
    else {
      return false;
    }
  }

}
