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

package cc.sfclub.packy.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author EvanLuo42
 * @date 2021/7/16 8:32 上午
 */
@Service
public class CaptchaSendService {
    public boolean sendCaptcha(String to, String from, String host, String authKey, String captcha) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", host);
            Session session = Session.getInstance(props);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            Transport transport = session.getTransport();

            msg.setFrom(new InternetAddress(from, "Packy Group", "UTF-8"));
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to, to, "UTF-8"));
            msg.setSubject("Packy邮箱验证");
            msg.setContent("验证码: " + captcha, "text/html;charset=UTF-8");
            msg.saveChanges();

            transport.connect(from, authKey);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
