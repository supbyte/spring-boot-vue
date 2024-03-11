package com.lmw.config;

import com.alibaba.fastjson2.JSONObject;
import com.lmw.entity.RestBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth->{
            auth.anyRequest().authenticated();
        }).formLogin(conf->{
            conf.loginProcessingUrl("/api/auth/login");
            conf.successHandler(this::onAuthSuccess);
            conf.failureHandler(this::onAuthFailure);
        }).exceptionHandling(conf->{
            conf.authenticationEntryPoint(this::onAuthFailure);
        }).logout(conf->{
            conf.logoutUrl("/api/auth/logout");
        }).csrf(conf->{
            conf.disable();
        }).build();
    }

    public void onAuthSuccess(HttpServletRequest request,
                              HttpServletResponse response,
                              Authentication authentication) throws IOException {
        response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
    }

    public void onAuthFailure(HttpServletRequest request,
                              HttpServletResponse response,
                              AuthenticationException exception) throws IOException {
        response.getWriter()
                .write(JSONObject.toJSONString(RestBean.failure(401,exception.getMessage())));
    }




}
