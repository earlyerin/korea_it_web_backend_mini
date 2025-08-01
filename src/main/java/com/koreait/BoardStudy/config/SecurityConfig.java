package com.koreait.BoardStudy.config;

import com.koreait.BoardStudy.security.filter.JwtAuthenticationFilter;
import com.koreait.BoardStudy.security.handler.OAuth2SuccessHandler;
import com.koreait.BoardStudy.service.Oauth2PrincipalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private Oauth2PrincipalUserService oauth2PrincipalUserService;

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //CORS(Cross-Origin Resource Sharing) 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern(CorsConfiguration.ALL);
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    //Custom SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception{ //HttpSecurity 클래스에 예외를 던지는 메서드를 사용하므로 예외 처리
        http.cors(Customizer.withDefaults()); //cors 설정 적용
        http.csrf(csrf -> csrf.disable()); //csrf 보호 비활성화
        http.formLogin(formLogin -> formLogin.disable()); //SSR 방식 로그인 해제
        http.logout(logout -> logout.disable()); //SSR 방식 로그아웃 해제
        http.httpBasic(httpBasic -> httpBasic.disable()); //HTTP 프로토콜 기본 로그인 해제
        http.sessionManagement(session
                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //비상태 방식(세션 해제)

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/auth/**",
                                        "/oauth2/**",
                                         "/login/oauth2/**",
                                        "/mail/verify"
                                        ).permitAll();
            auth.anyRequest().authenticated();
        }); //특정 URL에 대한 설정

        http.oauth2Login(oauth2 ->
                oauth2.userInfoEndpoint(userInfo -> userInfo.userService(oauth2PrincipalUserService))
                        .successHandler(oAuth2SuccessHandler)
        );

        return http.build();
    }
}
