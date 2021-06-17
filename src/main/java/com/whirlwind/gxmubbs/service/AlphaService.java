package com.whirlwind.gxmubbs.service;

import com.whirlwind.gxmubbs.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Service
@Scope("prototype")//加了这个之后可以实例化多次bean 不常用
public class AlphaService {
    //service依赖于Dao
    @Autowired
    private AlphaDao alphaDao;

    public  AlphaService(){
        System.out.println("实例化AlphaService");
    }

    @PostConstruct()
    public  void init(){
        System.out.println("初始化AlphaService");
    }
//销毁前触发
    @PreDestroy
    public  void destroy(){
        System.out.println("销毁AlphaService");
    }

    public String find(){
        return alphaDao.select();
    }
}
