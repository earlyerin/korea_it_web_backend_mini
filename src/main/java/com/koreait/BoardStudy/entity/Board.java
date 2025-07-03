package com.koreait.BoardStudy.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
    private Integer boardId;
    private String title;
    private String content;
    private Integer userId;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
}
