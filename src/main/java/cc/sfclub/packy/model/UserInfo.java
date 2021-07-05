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

package cc.sfclub.packy.model;

import cc.sfclub.packy.Permission;
import lombok.Builder;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

/**
 * @author TODAY
 * @date 2021/7/5 12:55
 */
@Builder
public class UserInfo {
    /** 用户名 */
    public String name;
    /** 注册时间*/
    public Date joinTime;
    /** 发布的包 */
    public List<PackageInfo> publishedPackages;
    /** 个人简介 */
    public String bio;
    /** 邮箱 */
    public String email;
    /** 权限 */
    public Permission permissionLevel;
}
