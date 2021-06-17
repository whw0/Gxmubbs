package com.whirlwind.gxmubbs.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
//加了这个注解容器会扫描并加载该bean
@Repository
//这时候已经有两个bean了
//我们要使用Mybatis
//这里加
@Primary
public class AlphaDaoMyBatisImpl implements AlphaDao{
    @Override
    public String select() {
        return "MyBatis";
    }
}
