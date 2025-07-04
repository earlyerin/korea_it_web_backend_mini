package com.koreait.BoardStudy.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OAuth2User {
    private Integer oauth2UserId;
    private Integer userId;
    private String provider;
    private String providerUserId;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
}
