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

package cc.sfclub.packy.interceptor;

import cc.sfclub.packy.Json;
import cc.sfclub.packy.dao.UserRepository;
import cc.sfclub.packy.ex.UnauthorizedException;
import cc.sfclub.packy.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @author EvanLuo42
 * @date 2021/7/15 4:35 下午
 */
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        try {
            if (token != null){
                String username = JwtUtils.getUserNameByToken(request);
                if (JwtUtils.verify(
                        token,
                        username,
                        userRepository.queryByUserName(username).getPerm(),
                        userRepository.queryByUserName(username).getPassword()
                )) {
                    return true;
                } else {
                    throw new UnauthorizedException();
                }
            } else {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(401);
                PrintWriter writer = response.getWriter();

                writer.write(Json.notFound("Token not found.").toString());
            }
        } catch (Exception e) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(401);
            PrintWriter writer = response.getWriter();

            writer.write(Json.unauthorized("Token incorrect.").toString());
        }

        return false;
    }
}
