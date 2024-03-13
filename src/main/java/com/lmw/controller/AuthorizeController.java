package com.lmw.controller;

import com.lmw.entity.Account;
import com.lmw.entity.RestBean;
import com.lmw.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public RestBean<String> validateRegisterEmail(@Pattern(regexp = EMAIL_REGEXP) String email,
                                          HttpSession session){
        //返回值为null表示该方法执行成功，邮件已正常发送
        if (authorizeService.sendValidateEmail(email, session.getId(),false)==null){
            return RestBean.success("邮件已发送，请注意查收");
        }else {
            return RestBean.failure(400,"邮件发送失败，请联系管理员");
        }
    }

    @PostMapping("/valid-reset-email")
    public RestBean<String> validateResetEmail(@Pattern(regexp = EMAIL_REGEXP) String email,
                                                  HttpSession session){
        //返回值为null表示该方法执行成功，邮件已正常发送
        String errorMessage = authorizeService.sendValidateEmail(email, session.getId(), true);
        if (errorMessage==null){
            return RestBean.success("邮件已发送，请注意查收");
        }else {
            return RestBean.failure(400,errorMessage);
        }
    }


    @PostMapping("/register")
    public RestBean<String> registerUser(@Valid Account account, String code, HttpSession session){
        //返回值为null说明方法执行成功，用户注册完成
        String errorMessage = authorizeService.validateAndRegister(account,code,session.getId());
        if (errorMessage==null){
            return RestBean.success("注册成功");
        }else {
            return RestBean.failure(400,errorMessage);
        }
    }

    /**
     * 1.发验证邮件
     * 2.检查验证码是否正确，正确就在Session中存一个标记
     * 3.用户发起重置密码请求，如果存在标记就允许重置
     */
    @PostMapping("/start-reset")
    public RestBean<String> startReset(@Pattern(regexp = EMAIL_REGEXP)@RequestParam("email") String email,
                                       @Length(min = 6,max = 6) @RequestParam("code") String code,
                                       HttpSession session){
        String errorMessage = authorizeService.validateOnly(email, code, session.getId());
        if (errorMessage == null){
            session.setAttribute("reset-password",email);
            return RestBean.success();
        }
        return RestBean.failure(400,errorMessage);
    }

    @PostMapping("/do-reset")
    public RestBean<String> resetPassword(@Length(min = 6,max = 16)@RequestParam("password")String password,
                                          HttpSession session){
        String email = (String) session.getAttribute("reset-password");
        if (email == null){
            return RestBean.failure(401,"请先完成邮箱验证");
        }else if(authorizeService.resetPassword(password,email)) {
            session.removeAttribute("reset-password");
            return RestBean.success("密码重置成功");
        }
        return RestBean.failure(500,"内部错误，请联系管理员");
    }

}
