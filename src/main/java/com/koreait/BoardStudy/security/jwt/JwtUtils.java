package com.koreait.BoardStudy.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private final Key KEY;

    public JwtUtils(@Value("${jwt.secret}") String secretKey){
        KEY = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        /*
        Keys : jjwt의 암호화된 키 유틸리티 클래스
        -> hmacShaKeyFor() : 디코딩된 키의 객체를 생성하는 메서드
        -> Decoders.BASE64URL.decode(secretKey) : URL-safe base64 인코딩된 문자열을 바이트 형태로 디코딩
         */
    }

    public String generateAccessToken(String id){
        return Jwts.builder()
                .subject("AccessToken")
                .id(id)
                .expiration(new Date(new Date().getTime() + (1000L * 60L * 60L * 24L * 30L)))
                .signWith(KEY)
                .compact();
    }

    public boolean isBearer(String token){
        if(token == null){
            return false;
        }
        return token.startsWith("Bearer ");
    }

    public String removeBearer(String token){
        if (token == null){
            return "Invalid Token";
        }
        return token.replaceFirst("Bearer ", "");
    }

    public Claims getClaims(String token) {
        //파서 객체 생성
        JwtParser jwtParser = Jwts.parser()
                .verifyWith((SecretKey)KEY) //서명 검증용 키 설정
                .build();
        //Payload(Claims) 반환
        return jwtParser.parseSignedClaims(token) //토큰 파싱 및 서명 검증(예외 처리 필요)
                .getPayload();
    }

    public String generateVerifyToken(String id){
        return Jwts.builder()
                .subject("VerifyToken")
                .id(id)
                .expiration(new Date(new Date().getTime() + (1000L * 60L * 3L)))
                .signWith(KEY)
                .compact();
    }
}
