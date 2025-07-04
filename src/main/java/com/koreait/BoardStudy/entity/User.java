package com.koreait.BoardStudy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Integer userId;
    private String userName;
    @JsonIgnore
    private String password;
    private String userEmail;
    private LocalDateTime regDt;
    private LocalDateTime updDt;

    private List<UserRole> userRoles;
    //Entity에 DB와 1:1 매칭되지 않는 필드가 존재하므로 NoArgsConstructor 명시
}
