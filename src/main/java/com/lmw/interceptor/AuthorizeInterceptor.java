package com.lmw.interceptor;

import com.lmw.entity.user.AccountUser;
import com.lmw.mapper.AccountMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizeInterceptor implements HandlerInterceptor {

    @Resource
    AccountMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityContext context = SecurityContextHolder.getContext();
        User user = (User)context.getAuthentication().getPrincipal();
        String username = user.getUsername();
        AccountUser accountUser = mapper.findAccountUserByUsernameOrEmail(username);
        request.getSession().setAttribute("account",accountUser);
        return true;
    }
}
