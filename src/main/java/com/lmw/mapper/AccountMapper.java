package com.lmw.mapper;

import com.lmw.entity.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {

    @Select("select * from account where username=#{arg} or email=#{arg}")
    Account findAccountByUsernameOrEmail(String arg);

    @Insert("insert into account values (#{id},#{username},#{password},#{email},#{status})")
    int saveAccount(Account account);

    @Update("update account set password = #{password} where email = #{email}")
    int resetPasswordByEmail(@Param("password") String password, @Param("email") String email);
}
