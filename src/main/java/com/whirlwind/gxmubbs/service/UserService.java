package com.whirlwind.gxmubbs.service;

import com.whirlwind.gxmubbs.dao.UserMapper;
import com.whirlwind.gxmubbs.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }
}
