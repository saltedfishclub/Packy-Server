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

package cc.sfclub.packy.config;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

import cc.sfclub.packy.Constant;
import cc.sfclub.packy.Pageable;
import cc.sfclub.packy.ex.AccessForbiddenException;

/**
 * @author TODAY 2020/9/10 11:06
 */
public class PageableMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private int maxPageSize = 200;
  private int defaultListSize = Constant.DEFAULT_LIST_SIZE;

  @Override
  public boolean supportsParameter(final MethodParameter methodParameter) {
    return methodParameter.getParameterType() == Pageable.class;
  }

  @Override
  public Object resolveArgument(final MethodParameter methodParameter,
                                final ModelAndViewContainer modelAndViewContainer,
                                final NativeWebRequest nativeWebRequest,
                                final WebDataBinderFactory webDataBinderFactory) {
    return new NativeWebRequestPageable(nativeWebRequest);
  }

  public int getDefaultListSize() {
    return defaultListSize;
  }

  public void setDefaultListSize(final int defaultListSize) {
    this.defaultListSize = defaultListSize;
  }

  public int getMaxPageSize() {
    return maxPageSize;
  }

  public void setMaxPageSize(final int maxPageSize) {
    this.maxPageSize = maxPageSize;
  }

  private final class NativeWebRequestPageable implements Pageable {

    private Integer size;
    private Integer current;
    private final NativeWebRequest request;

    public NativeWebRequestPageable(NativeWebRequest request) {
      this.request = request;
    }

    @Override
    public int getCurrent() {

      if (current == null) {
        final String parameter = request.getParameter(Constant.PARAMETER_CURRENT);
        if (StringUtils.isEmpty(parameter)) {
          current = 1;
        }
        else if ((current = Integer.valueOf(parameter)) <= 0) {
          throw new IllegalArgumentException("only 'page > 0'");
        }
      }
      return current;
    }

    @Override
    public int getSize() {
      if (size == null) {
        int s;
        final String parameter = request.getParameter(Constant.PARAMETER_SIZE);
        if (StringUtils.isEmpty(parameter)) {
          s = defaultListSize;
        }
        else {
          s = Integer.parseInt(parameter);
          if (s <= 0) {
            throw new IllegalArgumentException("only 'size > 0'");
          }
          if (s > maxPageSize) {
            throw new AccessForbiddenException();
          }
        }
        return size = s;
      }
      return size;
    }

    // Object
    // ----------------------

    @Override
    public boolean equals(Object obj) {

      if (obj == this) {
        return true;
      }

      if (obj instanceof Pageable) {
        final Pageable other = (Pageable) obj;
        return Objects.equals(other.getCurrent(), this.current)
                && Objects.equals(other.getSize(), this.size);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(size, current);
    }

  }

}
