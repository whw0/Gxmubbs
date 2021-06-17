package com.whirlwind.gxmubbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration //表示这个类是一个配置类
public class AlphaConfig {

    //将这个外部类装配到容器当中
    @Bean
    //simpleDateFormat就是bean的名字
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy--MM-dd HH:mm:ss");
    }

}
