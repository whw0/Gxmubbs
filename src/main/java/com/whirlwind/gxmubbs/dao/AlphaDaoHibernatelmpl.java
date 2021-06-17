package com.whirlwind.gxmubbs.dao;

import org.springframework.stereotype.Repository;

//加了这个注解容器会扫描并加载该bean
//也可以给bean定义一个名字 默认名字是alphaDaoHibernatempl
@Repository("alphaHibernate")
public class AlphaDaoHibernatelmpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
