package com.lmw;

import com.lmw.mapper.AccountMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringbootBackendApplicationTests {

    @Resource
    AccountMapper accountMapper;

    @Test
    void contextLoads() {

//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println(encoder.encode("123456"));
//        System.out.println(accountMapper.findAccountByUsernameOrEmail("admin"));
    }


}
