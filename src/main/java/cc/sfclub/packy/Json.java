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

import java.util.function.Function;

/**
 * @author TODAY 2020/9/10 11:24
 */
public class Json implements Result {

  private static final long serialVersionUID = 1L;

  private Object data;
  private String message;
  private boolean success;

  @Override
  public Object getData() {
    return data;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }

  public Json data(Object data) {
    this.data = data;
    return this;
  }

  public Json message(String message) {
    this.message = message;
    return this;
  }

  public Json success(boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Apply the common {@link Json} result
   *
   * @param <T>
   * @param func
   *   the {@link Function}
   * @param param
   *   parameter
   *
   * @return
   */
  public static final <T> Json apply(Function<T, Boolean> func, T param) {
    if (func.apply(param)) {
      return Json.ok();
    }
    return Json.failed();
  }

  /**
   * @param <T>
   * @param success
   *
   * @return
   */
  public static final <T> Json apply(boolean success) {
    if (success) {
      return Json.ok();
    }
    return Json.failed();
  }

  /**
   * @param success
   *   if success
   * @param message
   *   the message of the response
   * @param data
   *   response data
   */
  public static Json create(boolean success, String message, Object data) {
    return new Json()
      .data(data)
      .message(message)
      .success(success);
  }

  public static Json ok() {
    return create(true, OPERATION_OK, null);
  }

  public static Json ok(String message, Object data) {
    return create(true, message, data);
  }

  public static Json ok(Object data) {
    return create(true, OPERATION_OK, data);
  }

  public static Json ok(String message) {
    return create(true, message, null);
  }

  /**
   * default failed json
   */
  public static Json failed() {
    return create(false, OPERATION_FAILED, null);
  }

  public static Json failed(Object data) {
    return create(false, OPERATION_FAILED, data);
  }

  public static Json failed(String message) {
    return create(false, message, null);
  }

  public static Json failed(String message, Object data) {
    return create(false, message, data);
  }

  public static Json badRequest() {
    return badRequest(BAD_REQUEST);
  }

  /**
   * @param msg
   *
   * @return
   */
  public static Json badRequest(String msg) {
    return create(false, msg, null);
  }

  public static Json notFound() {
    return notFound(NOT_FOUND);
  }

  public static Json notFound(String msg) {
    return create(false, msg, null);
  }

  public static Json unauthorized() {
    return unauthorized(UNAUTHORIZED);
  }

  public static Json unauthorized(String msg) {
    return failed(msg, 401);
  }

  public static Json accessForbidden() {
    return accessForbidden(ACCESS_FORBIDDEN);
  }

  public static Json accessForbidden(String msg) {
    return failed(msg, 403);
  }

  @Override
  public String toString() {
    return new StringBuilder()//
      .append("{\"message\":\"").append(message)//
      .append("\",\"data\":\"").append(data)//
      .append("\",\"success\":\"").append(success)//
      .append("\"}")//
      .toString();
  }
}
