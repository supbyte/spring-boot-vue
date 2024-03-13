package com.lmw.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface AuthorizeService extends UserDetailsService {

    boolean sendValidateEmail(String email,String sessionId);
}
