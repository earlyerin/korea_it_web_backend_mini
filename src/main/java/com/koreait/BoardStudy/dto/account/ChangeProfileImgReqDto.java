package com.koreait.BoardStudy.dto.account;

import com.koreait.BoardStudy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeProfileImgReqDto {
    private Integer userId;
    private String profileImg;

    public User toEntity(){
        return  User.builder()
                .userId(userId)
                .profileImg(profileImg)
                .build();
    }
}
