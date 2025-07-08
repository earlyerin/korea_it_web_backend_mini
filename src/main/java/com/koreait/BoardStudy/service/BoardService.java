package com.koreait.BoardStudy.service;

import com.koreait.BoardStudy.dto.ApiRespDto;
import com.koreait.BoardStudy.dto.board.AddBoardReqDto;
import com.koreait.BoardStudy.dto.board.BoardRespDto;
import com.koreait.BoardStudy.entity.Board;
import com.koreait.BoardStudy.entity.User;
import com.koreait.BoardStudy.repository.BoardRepository;
import com.koreait.BoardStudy.repository.UserRepository;
import com.koreait.BoardStudy.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> addBoard(AddBoardReqDto addBoardReqDto, PrincipalUser principalUser){
        if(principalUser == null || !addBoardReqDto.getUserId().equals(principalUser.getUserId())){
            return new ApiRespDto<>("failed", "잘못된 접근입니다. 로그인 정보가 유효하지 않거나 권한이 없습니다.", null);
        }

        if(addBoardReqDto.getTitle() == null || addBoardReqDto.getTitle().trim().isEmpty()){
            return new ApiRespDto<>("failed", "제목은 필수 입력 사항입니다.", null);
        }

        if(addBoardReqDto.getContent() == null || addBoardReqDto.getContent().trim().isEmpty()){
            return new ApiRespDto<>("failed", "내용은 필수 입력 사항입니다.", null);
        }

        try {
            int result = boardRepository.addBoard(addBoardReqDto.toEntity());
            if(result == 0){
                return new ApiRespDto<>("failed", "게시물 추가에 실패했습니다.", null);
            }
            return new ApiRespDto<>("success", "게시물 추가를 완료했습니다.", null);
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "서버오류로 게시물 춫가에 실패했습니다." + e.getMessage(), null);
        }
    }

    public ApiRespDto<?> getBoardByBoardId(Integer boardId){
        if(boardId == null || boardId <= 0){
            return new ApiRespDto<>("failed", "유효하지 않는 게시물 아이디입니다.", null);
        }

        Optional<Board> optionalBoard = boardRepository.getBoardByBoardId(boardId);
        if(optionalBoard.isEmpty()){
            return new ApiRespDto<>("failed", "해당 아이디의 게시물은 존재하지 않습니다.", null);
        }
        Board board = optionalBoard.get();
        BoardRespDto boardRespDto = BoardRespDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .userName(board.getUser().getUserName())
                .regDt(board.getRegDt())
                .updDt(board.getRegDt())
                .build();
        return new ApiRespDto<>("success", "게시물 조회에 성공했습니다.", boardRespDto);
    }

    public ApiRespDto<?> getBoardList(){
        List<Board> boardList = boardRepository.getBoardList();
        if(boardList.isEmpty()){
            return new ApiRespDto<>("failed", "조회할 게시물이 존재하지 않습니다.", null);
        }

        return new ApiRespDto<>("success", "게시물 조회에 성공했습니다.", boardList);
    }


}
