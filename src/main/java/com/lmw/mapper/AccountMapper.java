package com.lmw.mapper;

import com.lmw.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {

    @Select("select * from account where username=#{arg} or email=#{arg}")
    Account findAccountByUsernameOrEmail(String arg);
}
