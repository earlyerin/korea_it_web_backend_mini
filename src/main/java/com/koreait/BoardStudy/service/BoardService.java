package com.koreait.BoardStudy.service;

import com.koreait.BoardStudy.dto.ApiRespDto;
import com.koreait.BoardStudy.dto.board.AddBoardReqDto;
import com.koreait.BoardStudy.dto.board.BoardRespDto;
import com.koreait.BoardStudy.dto.board.UpdateBoardReqDto;
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
            Optional<Board> optionalBoard = boardRepository.addBoard(addBoardReqDto.toEntity());
            if(optionalBoard.isEmpty()){
                return new ApiRespDto<>("failed", "게시물 추가에 실패했습니다.", null);
            }
            return new ApiRespDto<>("success", "게시물 추가를 완료했습니다.", optionalBoard.get().getBoardId());
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "서버오류로 게시물 춫가에 실패했습니다." + e.getMessage(), null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> removeBoardByBoardId(Integer boardId, PrincipalUser principalUser){
        //게시물 존재 여부
        Optional<Board> optionalBoard = boardRepository.getBoardByBoardId(boardId);
        if(optionalBoard.isEmpty()){
            return new ApiRespDto<>("failed", "존재하지 않는 게시물입니다.", null);
        }

        //사용자 정보 확인
        Board board = optionalBoard.get();
        if(!board.getUserId().equals(principalUser.getUserId())) {
            return new ApiRespDto<>("failed", "게시물을 삭제할 권한이 없습니다.", null);
        }

        try {
            int result = boardRepository.removeBoardByBoardId(boardId);
            if(result == 0){
                return new ApiRespDto<>("failed", "게시물 삭제에 실패했습니다.", null);
            }
            return new ApiRespDto<>("success", "게시물 삭제를 완료했습니다.", result);
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "서버오류로 게시물 삭제에 실패했습니다." + e.getMessage(), null);
        }
    }

    public ApiRespDto<?> updateBoardByBoardId(UpdateBoardReqDto updateBoardReqDto){
        //수정할 게시물 데이터 불러오기
        Optional<Board>  optionalBoard = boardRepository.getBoardByBoardId(updateBoardReqDto.getBoardId());
        if(optionalBoard.isEmpty()){
            return new ApiRespDto<>("failed", "게시물을 불러오는데 실패했습니다.", null);
        }
        Board board = optionalBoard.get();

        //수정 내용을 담은 Board 객체 생성
        Board newBoard = Board.builder()
                .boardId(board.getBoardId())
                .title(updateBoardReqDto.getTitle())
                .content(updateBoardReqDto.getContent())
                .userId(board.getUserId())
                .regDt(board.getRegDt())
                .updDt(board.getUpdDt())
                .build();

        try {
            int result = boardRepository.updateBoardByBoardId(newBoard);
            if(result == 0){
                return new ApiRespDto<>("failed", "게시물 수정에 실패했습니다.", null);
            }
            return new ApiRespDto<>("success", "게시물 수정를 완료했습니다.", newBoard.getBoardId());
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "서버오류로 게시물 수정에 실패했습니다." + e.getMessage(), null);
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
                .userId(board.getUserId())
                .boardId(board.getBoardId())
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
