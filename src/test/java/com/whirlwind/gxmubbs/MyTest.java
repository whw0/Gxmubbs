package com.whirlwind.gxmubbs;

import com.whirlwind.gxmubbs.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes=GxmubbsApplication.class)
public class MyTest {
    @Test
    public void testDate(){
        Date date=new Date();
        System.out.println(date);
    }

    @Autowired
    private MailClient mailClient;

    @Test
    public void testTextMail(){
        mailClient.sendMail("1449979716@qq.com","Test","这是一条测试");
    }
}
