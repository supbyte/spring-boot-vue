package com.lmw.controller;

import com.lmw.entity.RestBean;
import com.lmw.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final static String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

    @Resource
    AuthorizeService authorizeService;


    @PostMapping("/valid-register-email")
    public RestBean<String> vaildateEmail(@Pattern(regexp = EMAIL_REGEXP) String email,
                                          HttpSession session){
        if (authorizeService.sendValidateEmail(email, session.getId())){
            return RestBean.success("邮件已发送，请注意查收");
        }else {
            return RestBean.failure(400,"邮件发送失败，请联系管理员");
        }
    }
}
