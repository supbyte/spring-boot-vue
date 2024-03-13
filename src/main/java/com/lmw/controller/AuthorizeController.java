package com.lmw.controller;

import com.lmw.entity.Account;
import com.lmw.entity.RestBean;
import com.lmw.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final static String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private final static String USERNAME_REGEXP = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";

    @Resource
    AuthorizeService authorizeService;


    @PostMapping("/valid-register-email")
    public RestBean<String> vaildateEmail(@Pattern(regexp = EMAIL_REGEXP) String email,
                                          HttpSession session){
        //返回值为null表示该方法执行成功，邮件已正常发送
        if (authorizeService.sendValidateEmail(email, session.getId())==null){
            return RestBean.success("邮件已发送，请注意查收");
        }else {
            return RestBean.failure(400,"邮件发送失败，请联系管理员");
        }
    }

    @PostMapping("/register")
    public RestBean<String> registerUser(@Valid Account account, String code, HttpSession session){
        //返回值为null说明方法执行成功，用户注册完成
        if (authorizeService.validateAndRegister(account,code,session.getId())==null){
            return RestBean.success("注册成功");
        }else {
            return RestBean.failure(400,"注册失败，验证码填写错误");
        }
    }
}
