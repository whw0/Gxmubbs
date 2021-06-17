package com.whirlwind.gxmubbs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

//@Component 让spring管理，在哪个层次都可以用
@Component
public class MailClient {
    private static final Logger logger= LoggerFactory.getLogger(MailClient.class);

    /**
     * 邮件发送器
     */
    @Autowired
    private JavaMailSender mailSender;

    @Value ("${spring.mail.username}")
    private String from;

    /**
     *
     * @param to  邮件接收人
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendMail(String to,String subject,String content){
        //邮件信息
        MimeMessage message=mailSender.createMimeMessage();
        //邮件信息编辑器
        MimeMessageHelper helper=new MimeMessageHelper(message);

        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("邮件发送失败"+e.getMessage());
        }
    }
}
