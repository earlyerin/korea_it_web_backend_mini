package com.koreait.BoardStudy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
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
}
