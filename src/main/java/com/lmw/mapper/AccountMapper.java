package com.lmw.mapper;

import com.lmw.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {

    @Select("select * from account where username=#{arg} or email=#{arg}")
    Account findAccountByUsernameOrEmail(String arg);

    @Insert("insert into account values (#{id},#{username},#{password},#{email},#{status})")
    int saveAccount(Account account);
}
