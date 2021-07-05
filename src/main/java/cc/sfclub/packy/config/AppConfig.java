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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletContext;

import cc.sfclub.packy.annotation.Required;
import cc.sfclub.packy.ex.ApplicationException;
import cc.sfclub.packy.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TODAY 2021/7/5 11:50
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableAsync/*(mode = AdviceMode.ASPECTJ)*/
@EnableTransactionManagement/*(mode = AdviceMode.ASPECTJ)*/
public class AppConfig implements WebMvcConfigurer, ServletContextAware, AsyncConfigurer {

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return (Throwable ex, Method method, Object... params) -> {
      log.error("异步执行错误 方法: [{}],参数: [{}]", method, params, ex);
    };
  }

//  @Override
//  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//    registry.addResourceHandler("/upload/**")
//            .addResourceLocations("file:///" + upload + "/upload/");
//  }


//  @Override
//  public void addCorsMappings(final CorsRegistry registry) {
//
//    registry.addMapping("/api/**")
//      //            .allowedOrigins("*")
//      .allowedOrigins("http://localhost",
//                      "http://localhost:3000",
//                      "http://192.168.0.105:3000",
//                      "http://localhost:8000",
//                      "http://localhost:3001")
//      .allowCredentials(true)
//      .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//      .allowedHeaders("*");
//  }

  /**
   * Json序列化和反序列化转换器，用于转换Post请求体中的json以及将我们的对象序列化为返回响应的json
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    //LocalDateTime系列序列化和反序列化模块，继承自jsr310，我们在这里修改了日期格式
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule
            .addSerializer(LocalDateTime.class,
                           new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)));
    javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT)));
    javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_TIME_FORMAT)));
    javaTimeModule
            .addDeserializer(LocalDateTime.class,
                             new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)));
    javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT)));
    javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_TIME_FORMAT)));

    //Date序列化和反序列化
    javaTimeModule.addSerializer(Date.class, new JsonSerializer<Date>() {
      @Override
      public void serialize(Date date, JsonGenerator jsonGenerator,
                            SerializerProvider serializerProvider) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.DEFAULT_DATE_TIME_FORMAT);
        String formattedDate = formatter.format(date);
        jsonGenerator.writeString(formattedDate);
      }
    });
    javaTimeModule.addDeserializer(Date.class, new JsonDeserializer<Date>() {
      @Override
      public Date deserialize(JsonParser jsonParser,
                              DeserializationContext deserializationContext) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.DEFAULT_DATE_TIME_FORMAT);
        String date = jsonParser.getText();
        try {
          return format.parse(date);
        }
        catch (ParseException e) {
          throw new ApplicationException(e);
        }
      }
    });

    SimpleModule simpleModule = new SimpleModule();

    simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
    simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

    objectMapper.registerModule(simpleModule);
    objectMapper.registerModule(javaTimeModule);
    return objectMapper;
  }

  @Bean
  public TaskExecutorCustomizer taskExecutorCustomizer() {
    return taskExecutor -> taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
  }

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new UserMethodArgumentResolver());
    resolvers.add(new PageableMethodArgumentResolver());
    resolvers.add(new HandlerMethodArgumentResolver() {
      @Override
      public boolean supportsParameter(final MethodParameter parameter) {
        final Class<?> parameterType = parameter.getParameterType();
        return parameterType == LocalDateTime.class
                || parameterType == LocalDate.class
                || parameterType == LocalTime.class;
      }

      @Override
      public Object resolveArgument(final MethodParameter parameter,
                                    final ModelAndViewContainer modelAndViewContainer,
                                    final NativeWebRequest nativeWebRequest,
                                    final WebDataBinderFactory webDataBinderFactory) {

        final String parameterName = parameter.getParameterName();
        final String requestParameter = nativeWebRequest.getParameter(parameterName);

        // 校验是否为null
        if (requestParameter == null) {
          if (parameter.hasMethodAnnotation(Required.class)) {
            throw ApplicationException.failed("参数'" + parameterName + "'不能为空");
          }
          return null;
        }

        final Class<?> parameterType = parameter.getParameterType();
        if (parameterType == LocalDate.class) {
          return LocalDate.from(DateUtils.dateFormatter.parse(requestParameter));
        }
        if (parameterType == LocalTime.class) {
          return LocalTime.from(DateUtils.timeFormatter.parse(requestParameter));
        }
        if (parameterType == LocalDateTime.class) {
          return LocalDateTime.from(DateUtils.dateTimeFormatter.parse(requestParameter));
        }
        throw ApplicationException.failed("系统错误");
      }
    });
  }

  @Override
  public void setServletContext(ServletContext servlet) {
//    servlet.setRequestCharacterEncoding(Constant.DEFAULT_ENCODING);
//    servlet.setResponseCharacterEncoding(Constant.DEFAULT_ENCODING);
  }

}

