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

package cc.sfclub.packy.controllers;

import cc.sfclub.packy.ex.UnauthorizedException;
import cc.sfclub.packy.mapper.UserMapper;
import cc.sfclub.packy.model.JwtDetail;
import cc.sfclub.packy.model.UserInfo;
import cc.sfclub.packy.model.UserLogin;
import cc.sfclub.packy.utils.EncryptUtils;
import cc.sfclub.packy.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cc.sfclub.packy.ex.NotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author EvanLuo42 2021/7/5 11:51
 */
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserMapper userMapper;

  @GetMapping(value = "/{id}")
  public UserInfo getUserById(@PathVariable int id) {
    UserInfo userInfo = userMapper.getUserById(id);

    if (userInfo == null) {
      throw new NotFoundException("用户不存在");
    }

    return userInfo;
  }
  @PostMapping(value = "/login")
  public Map<String, String> login(@RequestBody UserLogin body) {
      JwtDetail userLogin = userMapper.login(body.user);
      Map<String, String> response = new HashMap<>();

      if(userLogin != null) {
          if(userLogin.user_pass.equals(EncryptUtils.getSHA256Str(body.pass))) {
              String token = JwtUtils.sign(body.user, userLogin.user_perm, userLogin.user_pass);
              response.put("token", token);

              return response;
          } else {
              throw new UnauthorizedException("用户名或密码错误");
          }
      } else {
          throw new UnauthorizedException("用户名不存在");
      }
  }
}
