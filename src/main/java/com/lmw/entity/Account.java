package com.lmw.entity;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Data
public class Account {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 2,max = 8)
    private String username;

    /**
     *
     */
    @Length(min = 6,max = 16)
    private String password;

    /**
     *
     */
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    private String email;

    /**
     *
     */
    private String status;
}