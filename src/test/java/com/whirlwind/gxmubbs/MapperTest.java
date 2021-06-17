package com.whirlwind.gxmubbs;

import com.whirlwind.gxmubbs.dao.DiscussPostMapper;
import com.whirlwind.gxmubbs.dao.UserMapper;
import com.whirlwind.gxmubbs.entity.DiscussPost;
import com.whirlwind.gxmubbs.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes=GxmubbsApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectUser(){

    }

    @Test
    public void testInsertUser(){
        User user=new User();
        user.setUsername("Whirlwind");
        user.setPassword("dale128");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("https://www.nocoder.com/101.png");
        user.setCreateTime(new Date());
        int rows=userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testSelectPost(){
        List<DiscussPost> list= discussPostMapper.selectDiscussPosts(0,0,10);

        int rows=discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }

}
