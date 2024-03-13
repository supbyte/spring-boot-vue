package com.lmw.service;

import com.lmw.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface AuthorizeService extends UserDetailsService {

    String sendValidateEmail(String email, String sessionId);

    String validateAndRegister(Account account, String code, String sessionId);
}
