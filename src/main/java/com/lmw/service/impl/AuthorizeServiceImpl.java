package com.lmw.service.impl;

import com.lmw.entity.Account;
import com.lmw.mapper.AccountMapper;
import com.lmw.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from;

    @Resource
    AccountMapper mapper;
    @Resource
    JavaMailSender sender;
    @Resource
    StringRedisTemplate template;
    @Lazy
    @Resource
    BCryptPasswordEncoder encoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null){
            throw new UsernameNotFoundException("用户名不能为空");
        }
        Account account = mapper.findAccountByUsernameOrEmail(username);
        if (account == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    /*
     * 1.先生成对应的验证码
     * 2.把邮箱和对应的验证码直接放到 Redis里面（过期时间三分钟，剩余两分钟后可要求重发邮件）
     * 3.发送验证码到指定邮箱
     * 4.如果发送失败，把 Redis中的刚刚插入的数据删除
     * 5.用户在注册时，再从Redis里面取出键值对，查看验证码是否一致
     * */
    @Override
    public String sendValidateEmail(String email, String sessionId) {
        // 构建Redis键
        String key = "email:" + sessionId + ":" + email;

        // 检查是否存在键，并检查过期时间
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            //获取Redis中键 key 的过期时间（以秒为单位），如果获取到的过期时间为null，则将其设为 0秒
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire > 120) {
                return "请求频繁，请稍后再试"; // 如果过期时间大于120秒（2分钟），直接返回false
            }
        }

        //查看数据库是否以及存在该用户
        if (mapper.findAccountByUsernameOrEmail(email) != null){
            return "此邮箱已被其他用户注册";
        }

        // 生成随机验证码
        Random random = new Random();
        int code = random.nextInt(899999) + 10000;

        // 创建邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("【登录验证码】有效时间3分钟请注意保存！"); // 设置邮件主题
        message.setText("您的验证码为：" + code); // 设置邮件内容，包含随机生成的验证码
        message.setFrom(from); // 设置发件人
        message.setTo(email); // 设置收件人

        try {
            // 发送邮件
            sender.send(message); // 调用邮件发送方法

            // 将验证码存入Redis并设置过期时间为3分钟
            template.opsForValue().set(key, String.valueOf(code), 3, TimeUnit.MINUTES);

            return null; // 发送成功，返回true
        } catch (MailException e) {
            e.printStackTrace(); // 打印异常信息
            return "邮件发送失败，检查邮件地址是否有效"; // 发送失败，返回false
        }
    }

    @Override
    public String validateAndRegister(Account account, String code, String sessionId) {
        //构建 Redis 键
        String key = "email:" + sessionId + ":" + account.getEmail();
        //判断是否存在该键值对
        if (Boolean.TRUE.equals(template.hasKey(key))){
            //通过键获取值
            String value = template.opsForValue().get(key);
            //若值为null说明此时验证码正好过期
            if (value==null){
                return "验证码失效，请重新登录";
            //不为空则判断前后端验证码是否一致
            }else if (value.equals(code)){
                //存储数据之前先将密码加密
                account.setPassword(encoder.encode(account.getPassword()));
                //返回值大于0说明保存成功
                if (mapper.saveAccount(account)>0){
                    return null;
                }
                return "内部错误，请联系管理员";
            }else {
                return "验证码错误，请重新提交";
            }
        }
        return "请先获取验证码";
    }

}
