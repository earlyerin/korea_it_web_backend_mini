package com.koreait.BoardStudy.dto.board;

import com.koreait.BoardStudy.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BoardRespDto {
    private String title;
    private String content;
    private String userName;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
}
