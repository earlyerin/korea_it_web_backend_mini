package com.koreait.BoardStudy.dto.oauth2;

import com.koreait.BoardStudy.entity.OAuth2User;
import jakarta.annotation.sql.DataSourceDefinition;
import lombok.Data;

@Data
public class OAuth2MergeReqDto {
    private String userName;
    private String password;
    private String provider;
    private String providerUserId;

    public OAuth2User toEntityOAuth2User(Integer userId){
        return OAuth2User.builder()
                .userId(userId)
                .provider(provider)
                .providerUserId(providerUserId)
                .build();
    }


}
