package com.lmw.config;

import com.alibaba.fastjson2.JSONObject;
import com.lmw.entity.RestBean;
import com.lmw.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.sql.DataSource;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;
    @Resource
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PersistentTokenRepository repository) throws Exception {
        return http.authorizeHttpRequests(auth->{
            auth.requestMatchers("/api/auth/**").permitAll();
            auth.anyRequest().authenticated();
        }).formLogin(conf->{
            conf.loginProcessingUrl("/api/auth/login"); //登录请求路径
            conf.successHandler(this::onAuthSuccess);   //成功时调用
            conf.failureHandler(this::onAuthFailure);   //失败时调用
        }).exceptionHandling(conf->{
            conf.authenticationEntryPoint(this::onAuthFailure); //异常时调用
        }).logout(conf->{
            conf.logoutUrl("/api/auth/logout"); //登出请求路径
            conf.logoutSuccessHandler(this::onAuthSuccess); //成功时调用
        }).rememberMe(conf->{
            conf.rememberMeParameter("remember");   //浏览器存储的Cookie变量名
            conf.tokenValiditySeconds(3600*24*7);   //Cookie有效时间：7天
            conf.tokenRepository(repository);   //通过数据库实现持久化存储Cookie
        }).csrf(conf->{
            conf.disable(); //关闭浏览器csrf防御
        }).cors(conf->{
            conf.configurationSource(this.configurationSource());   //配置跨域请求
        }).userDetailsService(authorizeService).build();    //自定义登录方法
    }


    //授权成功时方法
    public void onAuthSuccess(HttpServletRequest request,
                              HttpServletResponse response,
                              Authentication authentication) throws IOException {
        if (request.getRequestURI().endsWith("/login")){
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
        }else if (request.getRequestURI().endsWith("/logout")){
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出登录成功")));
        }

    }

    //授权失败时方法
    public void onAuthFailure(HttpServletRequest request,
                              HttpServletResponse response,
                              AuthenticationException exception) throws IOException {
        response.getWriter()
                .write(JSONObject.toJSONString(RestBean.failure(401,exception.getMessage())));
    }

    //用户密码加密
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //持久化存储方法
    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    //跨域请求配置
    private CorsConfigurationSource configurationSource(){
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOrigin("http://localhost:8081");
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",cors);
        return source;
    }




}
