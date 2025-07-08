package com.koreait.BoardStudy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private Integer boardId;
    private String title;
    private String content;
    @JsonIgnore
    private Integer userId;
    private LocalDateTime regDt;
    private LocalDateTime updDt;

    private String userName;
}
