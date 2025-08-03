package com.koreait.BoardStudy.dto.account;

import lombok.Data;

@Data
public class FindPasswordReqDto {
    private String userName;
    private String userEmail;
}
