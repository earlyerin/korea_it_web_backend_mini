package com.koreait.BoardStudy.dto.account;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FindIdRespDto {
    private String userName;
    private LocalDateTime regDt;
}
