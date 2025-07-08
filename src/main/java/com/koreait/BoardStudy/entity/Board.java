package com.koreait.BoardStudy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Board {
    private Integer boardId;
    private String title;
    private String content;
    private Integer userId;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
}
