package com.whirlwind.gxmubbs.service;

import com.sun.jndi.url.dns.dnsURLContext;
import com.whirlwind.gxmubbs.dao.UserMapper;
import com.whirlwind.gxmubbs.entity.User;
import com.whirlwind.gxmubbs.util.CommunityUtil;
import com.whirlwind.gxmubbs.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${gxmubbs.path.domain}")
    private  String domain;

    @Value("&{server.servlet.context-path}")
    private  String contextPath;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String,Object> register(User user){
        Map<String,Object> map=new HashMap<>();

        if(user==null){
            throw new IllegalArgumentException("参数不能为null");
        }

        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空!");
            return map;
        }

        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空!");
            return map;
        }

        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空!");
            return map;
        }

        //验证
        User u=userMapper.selectByName(user.getUsername());
        if(u!=null){
            map.put("usernameMsg","账号已存在!");
            return map;
        }

        User m=userMapper.selectByEmail(user.getEmail());
        if(m!=null){
            map.put("emailMsg","邮箱已被注册");
            return map;
        }

        /**
         * 将用户信息存到数据库
         */

        //加密数据
        user.setSalt(CommunityUtil.createUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        //普通用户
        user.setType(0);
        //默认没有激活
        user.setStatus(0);
        //设置激活码
        user.setActivationCode(CommunityUtil.createUUID());
        //设置随机头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());

        //插入数据库
        userMapper.insertUser(user);

        //发送激活邮件 模板在templates->mail->activation

        Context context=new Context();
        context.setVariable("email",user.getEmail());
        String url=domain+contextPath+"/activation/"+user.getId()+"/"+ user.getActivationCode();
        context.setVariable("url",url);
        String content =templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"GXMUBBS账号激活",content);

        return map;
    }



}
