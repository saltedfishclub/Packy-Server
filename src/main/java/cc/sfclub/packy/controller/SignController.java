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

package cc.sfclub.packy.controller;

import cc.sfclub.packy.Json;
import cc.sfclub.packy.dao.UserRepository;
import cc.sfclub.packy.entity.UserEntity;
import cc.sfclub.packy.model.LoginReqBody;
import cc.sfclub.packy.model.RegisterReqBody;
import cc.sfclub.packy.service.CaptchaSendService;
import cc.sfclub.packy.service.LoginService;
import cc.sfclub.packy.utils.EncryptUtils;
import cc.sfclub.packy.utils.JwtUtils;
import cc.sfclub.packy.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author EvanLuo42
 * @date 2021/7/15 2:20 下午
 */
@RestController
public class SignController {
    UserRepository userRepository;
    LoginService loginService;
    CaptchaSendService captchaSendService;
    Environment env;

    @Autowired
    public SignController(UserRepository userRepository, LoginService loginService, CaptchaSendService captchaSendService, Environment env) {
        this.userRepository = userRepository;
        this.loginService = loginService;
        this.captchaSendService = captchaSendService;
        this.env = env;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Json login(@RequestBody LoginReqBody reqBody) {
        String userName = reqBody.getName();
        String passwordEncrypted = EncryptUtils.getSHA256Str(reqBody.getPass());
        UserEntity userEntitiesQuery = userRepository.queryByUserName(reqBody.getName());

        if (loginService.login(userName, passwordEncrypted, userEntitiesQuery)) {
            Map<String, String> data = new HashMap<>();
            data.put("token", JwtUtils.sign(userName, userEntitiesQuery.getPerm(), passwordEncrypted));

            return Json.ok("Login Successfully", data);
        }

        return Json.notFound("Username or password incorrect");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Json register(@RequestBody RegisterReqBody reqBody) {
        String userName = reqBody.getName();
        String passwordEncrypted = EncryptUtils.getSHA256Str(reqBody.getPass());
        String email = reqBody.getEmail();
        String captcha = UuidUtils.getUUID32();

        UserEntity userEntity = UserEntity.builder()
                .userName(userName)
                .password(passwordEncrypted)
                .email(email)
                .joinTime(System.currentTimeMillis())
                .captcha(captcha)
                .build();

        try {
            userRepository.save(userEntity);
            if (captchaSendService.sendCaptcha(
                    email,
                    env.getProperty("captcha.from"),
                    env.getProperty("captcha.host"),
                    env.getProperty("captcha.auth-key"),
                    captcha)) {
                return Json.ok("Register Successfully");
            }

            return Json.failed("Send captcha failed.");
        } catch (Exception e) {
            return Json.badRequest("This username has been used.");
        }
    }
}
