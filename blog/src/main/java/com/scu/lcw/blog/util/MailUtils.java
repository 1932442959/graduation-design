package com.scu.lcw.blog.util;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
@Slf4j
public class MailUtils {

    public String sendMail(String emailAddress) {
        //做链接前的准备工作  也就是参数初始化
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.qq.com");//发送邮箱服务器
        properties.setProperty("mail.smtp.port", "465");//发送端口
        properties.setProperty("mail.smtp.auth", "true");//是否开启权限控制
        properties.setProperty("mail.debug", "true");//true 打印信息到控制台
        properties.setProperty("mail.transport", "smtp");//发送的协议是简单的邮件传输协议
        properties.setProperty("mail.smtp.ssl.enable", "true");
        //建立两点之间的链接
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("1932442959@qq.com", "ypchbcorhgpseebg");
            }
        });
        //创建邮件对象
        Message message = new MimeMessage(session);
        //设置发件人
        try {
            message.setFrom(new InternetAddress("1932442959@qq.com"));

            //设置收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));//收件人
            //设置主题
            message.setSubject("验证码");
            //设置邮件正文  第二个参数是邮件发送的类型
            String randomString = randomString();
            message.setContent(randomString, "text/html;charset=UTF-8");
            //发送一封邮件
            Transport transport = session.getTransport();
            transport.connect("1932442959@qq.com", "ypchbcorhgpseebg");
            Transport.send(message);
            return randomString;
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {

        }

        return "fail";
    }

    private String randomString() {
        return RandomUtil.randomString(6);
    }
}
