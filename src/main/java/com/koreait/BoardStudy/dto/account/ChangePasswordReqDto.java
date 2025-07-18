package com.koreait.BoardStudy.dto.account;

import com.koreait.BoardStudy.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class ChangePasswordReqDto {
    private Integer userId;
    private String oldPassword;
    private String newPassword;
    private String checkPassword;

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder){
        return User.builder()
                .userId(userId)
                .password(bCryptPasswordEncoder.encode(newPassword))
                .build();
    }
}
