package com.koreait.BoardStudy.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class Oauth2PrincipalUserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /*
         OAuth2로 로그인한 사용자 정보 파싱 및 저장
         */
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = null;
        String id = null;

        //공급자 분기문
        switch (provider){
            case "google" :
                id = attributes.get("sub").toString().trim();
                email = attributes.get("email").toString();
                break;
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                id = response.get("id").toString().trim();
                email = (String) response.get("email");
                break;
            case "kakao":
                id = attributes.get("id").toString().trim();
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                email = "rin050301@gmail.com";
                //카카오 개발자 센터 -> 동의 항목에서 앱 권한 신청 후 사용자 이메일 요청가능
                break;
        }
        System.out.println("providerUserId: " + id);

        //사용자 정보
        Map<String, Object> newAttributes = Map.of(
                "id", id,
                "email", email,
                "provider", provider
        );
        //권한
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_TEMPORARY"));

        //OAuth2User -> SecurityContextHolder
        return new DefaultOAuth2User(authorities, newAttributes, "id");
    }
}
