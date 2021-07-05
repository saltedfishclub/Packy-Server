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

package cc.sfclub.packy.ex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.sfclub.packy.ErrorMessage;
import cc.sfclub.packy.Json;
import cc.sfclub.packy.Result;
import cc.sfclub.packy.ValidationError;
import cc.sfclub.packy.utils.ObjectUtils;

@RestControllerAdvice
public class ApplicationExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

  public static final ErrorMessage needLogin = ErrorMessage.failed("需要登录");
  public static final ErrorMessage accessForbidden = ErrorMessage.failed("权限不足");
  public static final ErrorMessage argsErrorMessage = ErrorMessage.failed("参数错误");
  public static final ErrorMessage sizeExceeded = ErrorMessage.failed("上传文件大小超出限制");
  public static final ErrorMessage methodNotSupported = ErrorMessage.failed("请求方式不支持");
  public static final ErrorMessage internalServerError = ErrorMessage.failed("服务器内部异常");
  public static final ErrorMessage notWritableError = ErrorMessage.failed("数据无法正常返回到客户端");

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UnauthorizedException.class)
  public ErrorMessage unauthorized() {
    return needLogin;
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessForbiddenException.class)
  public ErrorMessage accessForbidden() {
    return accessForbidden;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ NotFoundException.class })
  public ErrorMessage notFound(NotFoundException exceededException) {
    return ErrorMessage.failed(exceededException.getMessage());
  }

  @ExceptionHandler(InternalServerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage internal(InternalServerException internal) {
    log.error("服务器内部错误", internal);
    return ErrorMessage.failed(internal.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ MaxUploadSizeExceededException.class })
  public ErrorMessage badRequest() {
    return sizeExceeded;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ErrorMessage methodNotSupported() {
    return methodNotSupported;
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage error(Exception exception) {
    log.error("An Exception occurred", exception);
    if (exception instanceof SQLException) {
      return internalServerError;
    }
    if (exception instanceof HttpMessageNotWritableException) {
      return notWritableError;
    }
    if (exception instanceof TransactionSystemException) {
      return ErrorMessage.failed("数据库出错");
    }
    return ErrorMessage.failed(exception.getMessage());
  }

  /**
   * 不记录日志
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ ApplicationException.class, IllegalArgumentException.class, })
  public ErrorMessage badRequest(Exception exception) {
    return ErrorMessage.failed(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ErrorMessage typeMismatch(MethodArgumentTypeMismatchException mismatch) {
    return ErrorMessage.failed("参数'" + mismatch.getName() + "'不能转换到对应类型");
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ErrorMessage parameterError(MissingServletRequestParameterException e) {
    return ErrorMessage.failed("缺少参数'" + e.getParameterName() + "'");
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ MultipartException.class,
                            HttpMessageNotReadableException.class,
                            HttpMediaTypeNotSupportedException.class })
  public ErrorMessage messageNotReadable() {
    return argsErrorMessage;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ BindException.class, MethodArgumentNotValidException.class })
  public Result validExceptionHandler(Exception e) {

    final BindingResult result;
    if (e instanceof MethodArgumentNotValidException) {
      result = ((MethodArgumentNotValidException) e).getBindingResult();
    }
    else if (e instanceof BindException) {
      result = (BindingResult) e;
    }
    else {
      return ErrorMessage.failed();
    }
    final List<ObjectError> allErrors = result.getAllErrors();
    final Map<String, String> model = new HashMap<>(16);

    for (ObjectError error : allErrors) {
      if (error instanceof FieldError) {
        final FieldError fieldError = (FieldError) error;
        if (fieldError.contains(TypeMismatchException.class)) { // 类型不正确
          return ErrorMessage.failed("参数'" + fieldError.getField() + "'不能转换到对应类型");
        }
        final String field = fieldError.getField();
        final String defaultMessage = error.getDefaultMessage();
        model.put(field, defaultMessage);
        // log.error("[{}] -> [{}]", field, defaultMessage);
      }
    }
    return ValidationError.failed(model);
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Json nullPointer(NullPointerException exception) {
    final StackTraceElement[] stackTrace = exception.getStackTrace();
    if (ObjectUtils.isNotEmpty(stackTrace)) {
      return Json.failed("空指针", stackTrace[0]);
    }
    return Json.failed("空指针", "暂无堆栈信息");
  }

  @ExceptionHandler(DataAccessException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage dataAccessException(DataAccessException accessException) {
    final String message = getDataAccessMessage(accessException.getCause());
    log.error(message, accessException);
    return ErrorMessage.failed(message);
  }

  private String getDataAccessMessage(Throwable cause) {
    return "数据库出错";
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(DataAccessResourceFailureException.class)
  public ErrorMessage dataAccessException(DataAccessResourceFailureException accessException) {
    log.error("数据库连接出错", accessException);
    return ErrorMessage.failed("数据库连接出错");
  }
}
