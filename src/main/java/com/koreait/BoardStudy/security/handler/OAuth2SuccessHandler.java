package com.koreait.BoardStudy.security.handler;

import com.koreait.BoardStudy.entity.OAuth2User;
import com.koreait.BoardStudy.entity.User;
import com.koreait.BoardStudy.repository.Oauth2UserRepository;
import com.koreait.BoardStudy.repository.UserRepository;
import com.koreait.BoardStudy.security.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private Oauth2UserRepository oauth2UserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        /*
        OAuth2로 로그인한 사용자에 대해 서버 내부 인증 구현
         */
        //OAuth2User Attributes 가져오기
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String provider = defaultOAuth2User.getAttribute("provider");
        String providerUserId = defaultOAuth2User.getAttribute("id");
        String email = defaultOAuth2User.getAttribute("email");

        //클라이언트와 연동 확인
        Optional<OAuth2User> optionalOAuth2User = oauth2UserRepository
                .getOAuth2UserByProviderAndProviderUserId(provider, providerUserId);
        //공백이 인코딩되어 %20으로 DB 저장..
        if(optionalOAuth2User.isEmpty()){
            response.sendRedirect("http://localhost:5173/auth/oauth2?provider="
                    + provider + "&providerUserId=" + providerUserId + " &email=" + email);
            //회원가입 또는 연동 필요
            return;
        }

        //User DB 확인
        OAuth2User oAuth2User = optionalOAuth2User.get();
        Optional<User> optionalUser = userRepository.getUserByUserId(oAuth2User.getUserId());
        String accessToken = null;
        if(optionalUser.isPresent()){
            accessToken = jwtUtils.generateAccessToken(optionalUser.get().getUserId().toString());
        }

        response.sendRedirect("http://localhost:5173/auth/oauth2/signin?accessToken=" + accessToken);
        //이메일 인증 진행
    }
}
