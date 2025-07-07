package com.koreait.BoardStudy.dto.oauth2;

import com.koreait.BoardStudy.entity.OAuth2User;
import com.koreait.BoardStudy.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class OAuth2SignupReqDto {
    private String userName;
    private String password;
    private String userEmail;
    private String provider;
    private String providerUserId;

    public User toEntityUser(BCryptPasswordEncoder bCryptPasswordEncoder){
        return User.builder()
                .userName(userName)
                .password(bCryptPasswordEncoder.encode(password))
                .userEmail(userEmail)
                .build();
    }

    public OAuth2User toEntityOAuth2User(Integer userId){
        return OAuth2User.builder()
                .userId(userId)
                .provider(provider)
                .providerUserId(providerUserId)
                .build();
    }
}
