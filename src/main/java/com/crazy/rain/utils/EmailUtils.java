package com.crazy.rain.utils;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName: EmailUtils
 * @Description: 邮件工具类
 * @author: CrazyRain
 * @date: 2024/4/19 下午7:53
 */
@Component
public class EmailUtils {
    @Resource
    private MailSender mailSender;

    @Resource
    private MailProperties mailProperties;

    public int sendVerificationCode(String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setSubject("Api开放平台:");
        msg.setFrom(mailProperties.getUsername());
        msg.setTo(email);
        int code = RandomUtils.randomInt();
        msg.setText("您的邮箱验证码为" + code);
        mailSender.send(msg);
        return code;
    }
}
