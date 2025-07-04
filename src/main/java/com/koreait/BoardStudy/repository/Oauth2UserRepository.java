package com.koreait.BoardStudy.repository;

import com.koreait.BoardStudy.entity.OAuth2User;
import com.koreait.BoardStudy.mapper.OAuth2UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class Oauth2UserRepository {
    @Autowired
    private OAuth2UserMapper oAuth2UserMapper;

    public Optional<OAuth2User> getOAuth2UserByProviderAndProviderUserId
            (String provider, String providerUserId){
        return oAuth2UserMapper.getOAuth2UserByProviderAndProviderUserId(provider, providerUserId);
    }

    public int addOAuth2User(OAuth2User oAuth2User){
        return oAuth2UserMapper.addOAuth2User(oAuth2User);
    }
}
