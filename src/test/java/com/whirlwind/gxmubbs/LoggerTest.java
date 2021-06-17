package com.whirlwind.gxmubbs;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes=GxmubbsApplication.class)
public class LoggerTest {
    private static final Logger logger= LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void testLogger(){
        System.out.println(logger.getName());
        //因为已经设置了等级为debug trace则不会输出
        logger.trace("trace log");
        logger.debug("debug log");

    }
}
