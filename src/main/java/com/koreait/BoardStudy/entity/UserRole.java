package com.koreait.BoardStudy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    private Integer userRoleId;
    private Integer userId;
    private Integer roleId;
    private LocalDateTime regDt;
    private LocalDateTime updDt;

    private Role role;
    //Entity에 DB와 1:1 매칭되지 않는 필드가 존재하므로 NoArgsConstructor 명시
}
