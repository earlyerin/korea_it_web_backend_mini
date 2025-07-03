package com.koreait.BoardStudy.dto.auth;

import com.koreait.BoardStudy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
public class SignupReqDto {
    private String userName;
    private String password;
    private String userEmail;

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder){
        return User.builder()
                .userName(userName)
                .password(bCryptPasswordEncoder.encode(password))
                .userEmail(userEmail)
                .build();
    }
}
