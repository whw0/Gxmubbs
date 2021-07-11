package com.whirlwind.gxmubbs.service;

import com.sun.jndi.url.dns.dnsURLContext;
import com.whirlwind.gxmubbs.dao.LoginTicketMapper;
import com.whirlwind.gxmubbs.dao.UserMapper;
import com.whirlwind.gxmubbs.entity.LoginTicket;
import com.whirlwind.gxmubbs.entity.User;
import com.whirlwind.gxmubbs.util.CommunityConstant;
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
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${gxmubbs.path.domain}")
    private  String domain;

    @Value("${server.servlet.context-path}")
    private  String contextPath;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    //由id查询用户
    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    /**
     *注册功能
     * @param user
     * @return Map 返回一个map 如果有错误返回的map则不是空的
     */
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
        //通过/mail/activation下的html文件 其中html文件需要的变量用Context存储键值对送到html 产生模板
        String content =templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"GXMUBBS账号激活",content);

        return map;
    }


    /**
     * 激活处理
     * @param userId
     * @param code
     * @return
     */
    public  int activation(int userId,String code){
        User user=userMapper.selectById(userId);
        if(user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }
        else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }
        else{
            return ACTIVATION_FAILURE;
        }
    }

    //登录并生成登录凭证
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.createUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    //退出
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    //获取激活凭证
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }


//设置头像

    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }

    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }



}
