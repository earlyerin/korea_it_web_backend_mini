package com.koreait.BoardStudy.dto.auth;

import com.koreait.BoardStudy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SigninReqDto {
    private String userName;
    private String password;
}
