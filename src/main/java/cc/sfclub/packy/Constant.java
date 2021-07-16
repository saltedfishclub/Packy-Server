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

package cc.sfclub.packy;

import java.io.Serializable;

/**
 * @author TODAY 2021/7/5 11:51
 */
public interface Constant extends Serializable {

  /** 默认时间格式 */
  String DEFAULT_TIME_FORMAT = "HH:mm:ss";
  /** 默认日期格式 */
  String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
  /** 默认日期时间格式 */
  String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  String NOT_FOUND = "Not Found";
  String BAD_REQUEST = "Bad Request";
  String UNAUTHORIZED = "Unauthorized";
  String ACCESS_FORBIDDEN = "Access Forbidden";
  String METHOD_NOT_ALLOWED = "Method Not Allowed";
  String INTERNAL_SERVER_ERROR = "Internal Server Error";

  String OPERATION_OK = "ok";
  String OPERATION_FAILED = "failed";

}
