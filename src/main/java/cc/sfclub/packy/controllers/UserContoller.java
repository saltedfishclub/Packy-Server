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

import cc.sfclub.packy.Json;
import cc.sfclub.packy.Permission;
import cc.sfclub.packy.daos.UserTable;
import cc.sfclub.packy.model.UserInfo;
import cc.sfclub.packy.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author EvanLuo42
 * @date 2021/7/5 11:51
 */
@RestController
public class UserContoller {
    @Autowired
    private UserTable userTable;

    @GetMapping(value = "/api/v1/user/")
    public Json getUserById(@RequestParam(value = "id") int id) {
        UserInfo userInfo = userTable.getUserById(id);

        return Json.ok("Get User Successfully", userInfo);
    }

    @PostMapping(value = "/api/v1/login")
    public Json login(@RequestBody UserLogin body) {
        UserLogin userLogin = userTable.login(body.user);
        if(userLogin != null) {
            if(userLogin.pass.equals())
        }
    }
}
