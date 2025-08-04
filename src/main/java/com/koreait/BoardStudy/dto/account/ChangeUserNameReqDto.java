package com.koreait.BoardStudy.dto.account;

import com.koreait.BoardStudy.entity.User;
import lombok.Data;

@Data
public class ChangeUserNameReqDto {
    private Integer userId;
    private String userName;

    public User toEntity(){
        return User.builder()
                .userId(userId)
                .userName(userName)
                .build();
    }
}
