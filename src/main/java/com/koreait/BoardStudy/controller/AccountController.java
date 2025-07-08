package com.koreait.BoardStudy.controller;

import com.koreait.BoardStudy.dto.account.ChangePasswordReqDto;
import com.koreait.BoardStudy.security.model.PrincipalUser;
import com.koreait.BoardStudy.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController { //계정 관리 담당
    @Autowired
    private AccountService accountService;

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordReqDto changePasswordReqDto,
                                            @AuthenticationPrincipal PrincipalUser principalUser){
        return ResponseEntity.ok(accountService.updatePassword(changePasswordReqDto, principalUser));
    }
}
