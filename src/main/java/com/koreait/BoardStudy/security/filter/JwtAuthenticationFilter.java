package com.koreait.BoardStudy.security.filter;

import com.koreait.BoardStudy.entity.User;
import com.koreait.BoardStudy.repository.UserRepository;
import com.koreait.BoardStudy.security.jwt.JwtUtils;
import com.koreait.BoardStudy.security.model.PrincipalUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/*
 jakarta.servlet.Filter
 웹 애플리케이션의 요청과 응답 흐름을 가로채서 미들웨어 로직을 수행하는 인터페이스
 */
@Component
public class JwtAuthenticationFilter implements Filter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        //요청 확인
        HttpServletRequest request = (HttpServletRequest)  servletRequest;
        List<String> method = List.of("POST", "GET", "PUT", "PATCH", "DELETE");
        if(!method.contains(request.getMethod())){
            filterChain.doFilter(servletRequest, servletResponse); //다음 필터로 이동(응답 시 역순)
            return;
        }

        //토큰 인증
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String authorization = request.getHeader("Authorization"); //Authorization 헤더에 담긴 토큰 저장
        if(jwtUtils.isBearer(authorization)){
            String accessToken = jwtUtils.removeBearer(authorization);
            try {
                Claims claims = jwtUtils.getClaims(accessToken); //예외 처리
                Integer userId = Integer.parseInt(claims.getId());
                Optional<User> optionalUser = userRepository.getUserByUserId(userId);
                optionalUser.ifPresentOrElse((user) -> {
                    PrincipalUser principalUser = PrincipalUser.builder()
                            .userId(user.getUserId())
                            .password(user.getPassword())
                            .userEmail(user.getUserEmail())
                            .userName(user.getUserName())
                            .userRoles(user.getUserRoles())
                            .build();
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            principalUser, null, principalUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }, () -> { throw new AuthenticationServiceException("인증 실패");
                    //AuthenticationServiceException : 인증 처리 과정에서 발생하는 예외
                });
            } catch (IllegalArgumentException | JwtException e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //응답상태 : 401
                response.setContentType("application/json;charset=UTF=8"); //본문 타입 명시 : JSON
                response.getWriter().write("{\"error\": \"Invalid or expired token\"}"); //응답바디 : 에러 메세지 (JSON 형식으로 작성)
            }catch (RuntimeException e){
                e.printStackTrace();
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }
}
