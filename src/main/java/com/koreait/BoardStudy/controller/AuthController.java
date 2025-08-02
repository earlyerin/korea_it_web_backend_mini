package com.koreait.BoardStudy.controller;

import com.koreait.BoardStudy.dto.auth.SigninReqDto;
import com.koreait.BoardStudy.dto.auth.SignupReqDto;
import com.koreait.BoardStudy.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupReqDto signupReqDto){
        return ResponseEntity.ok(authService.signup(signupReqDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninReqDto signinReqDto){
        return ResponseEntity.ok(authService.signin(signinReqDto));
    }


}
