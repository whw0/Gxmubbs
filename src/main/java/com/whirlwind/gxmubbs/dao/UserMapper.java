package com.whirlwind.gxmubbs.dao;

import com.whirlwind.gxmubbs.entity.User;
import org.apache.ibatis.annotations.Mapper;

//用@Repository也可以
@Mapper
public interface UserMapper {
    //一些查询方法
    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);

    int insertUser(User user);

    //修改用户状态
    int updateStatus(int id,int status);
    int updateHeader(int id,String headerUrl);
    int updatePassword(int id,String password);

}
