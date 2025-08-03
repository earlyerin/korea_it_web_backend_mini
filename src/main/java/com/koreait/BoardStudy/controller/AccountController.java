package com.koreait.BoardStudy.controller;

import com.koreait.BoardStudy.dto.account.ChangePasswordReqDto;
import com.koreait.BoardStudy.dto.account.FindPasswordReqDto;
import com.koreait.BoardStudy.dto.account.UpdatePasswordReqDto;
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

    @PostMapping("/change/password/authentication/user")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordReqDto changePasswordReqDto,
                                            @AuthenticationPrincipal PrincipalUser principalUser){
        return ResponseEntity.ok(accountService.changePassword(changePasswordReqDto, principalUser));
    }

    @GetMapping("/find/id/{userEmail}")
    public ResponseEntity<?> findUserId(@PathVariable String userEmail){
        return ResponseEntity.ok(accountService.findUserId(userEmail));
    }

    @PostMapping("/find/password")
    public ResponseEntity<?> findPassword(@RequestBody FindPasswordReqDto findPasswordReqDto){
        return ResponseEntity.ok(accountService.findPassword(findPasswordReqDto));
    }

    @PostMapping("/update/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordReqDto updatePasswordReqDto,
                                            @AuthenticationPrincipal PrincipalUser principalUser){
        return ResponseEntity.ok(accountService.updatePassword(updatePasswordReqDto, principalUser));
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable Integer userId){
        return ResponseEntity.ok(accountService.getUserInfo(userId));
    }
}
