package com.lmw.service;

import com.lmw.entity.auth.Account;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface AuthorizeService extends UserDetailsService {

    String sendValidateEmail(String email, String sessionId, boolean hasAccount);

    String validateAndRegister(Account account, String code, String sessionId);

    String validateOnly(String email,String code, String sessionId);

    boolean resetPassword(String password,String email);
}