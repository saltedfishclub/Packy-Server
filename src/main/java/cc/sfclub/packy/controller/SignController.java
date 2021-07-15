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
import cc.sfclub.packy.utils.EncryptUtils;
import cc.sfclub.packy.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author EvanLuo42
 * @date 2021/7/15 2:20 下午
 */
@RestController
public class SignController {
    UserRepository userRepository;

    @Autowired
    public SignController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Json login(@RequestBody LoginReqBody loginReqBody) {
        String userName = loginReqBody.getName();
        String passwordEncrypted = EncryptUtils.getSHA256Str(loginReqBody.getPass());
        UserEntity userEntitiesQuery = userRepository.queryByUserName(loginReqBody.getName());

        try {
            if (userEntitiesQuery.getPassword().equals(passwordEncrypted)) {
                String token = JwtUtils.sign(
                        userName,
                        userEntitiesQuery.getPerm(),
                        passwordEncrypted
                );

                HashMap<String, String> data = new HashMap<>();
                data.put("token", token);

                return Json.ok("Login Successfully.", data);
            } else {
                return Json.failed("Username or password incorrect");
            }
        } catch (Exception exception) {
            return Json.failed("Username or password incorrect.");
        }
    }
}
