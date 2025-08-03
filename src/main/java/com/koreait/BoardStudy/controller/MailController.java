package com.koreait.BoardStudy.controller;

import com.koreait.BoardStudy.dto.mail.SendMailReqDto;
import com.koreait.BoardStudy.security.model.PrincipalUser;
import com.koreait.BoardStudy.service.MailService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMail(@RequestBody SendMailReqDto sendMailReqDto,
                                      @AuthenticationPrincipal PrincipalUser principalUser){
        return ResponseEntity.ok(mailService.sendMail(sendMailReqDto, principalUser));
    }

    @GetMapping("/verify")
    public String verify(Model model, @RequestParam String verifyToken){
        Map<String, Object> resultMap = mailService.verify(verifyToken);
        model.addAllAttributes(resultMap);
        return "result_page";
    }

    @PostMapping("/send/find/password")
    public ResponseEntity<?> sendToken(@RequestBody SendMailReqDto sendMailReqDto,
                                      @AuthenticationPrincipal PrincipalUser principalUser){
        return ResponseEntity.ok(mailService.sendToken(sendMailReqDto, principalUser));
    }

    @GetMapping("/verify/find/password/{verifyToken}")
    public ResponseEntity<?> verifyToken(@PathVariable String verifyToken){
        return ResponseEntity.ok(mailService.verifyToken(verifyToken));
    }
}
