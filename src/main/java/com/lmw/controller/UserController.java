package com.lmw.controller;

import com.lmw.entity.RestBean;
import com.lmw.entity.user.AccountUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @GetMapping("/me")
    public RestBean<AccountUser> me(@SessionAttribute AccountUser account){
        return RestBean.success(account);
    }
}
