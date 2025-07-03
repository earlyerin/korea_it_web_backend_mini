package com.koreait.BoardStudy.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserRole {
    private Integer userRoleId;
    private Integer userId;
    private Integer roleId;
    private LocalDateTime regDt;
    private LocalDateTime updDt;

    private Role role;
}
