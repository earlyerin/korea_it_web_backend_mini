package com.koreait.BoardStudy.repository;

import com.koreait.BoardStudy.dto.board.AddBoardReqDto;
import com.koreait.BoardStudy.entity.Board;
import com.koreait.BoardStudy.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepository {
    @Autowired
    private BoardMapper boardMapper;
    public Optional<Board> addBoard(Board board){
        try{
            int result = boardMapper.addBoard(board);
            if(result == 0){
                return Optional.empty();
            }
        }catch (DuplicateKeyException e){ //중복키 예외 처리
            return Optional.empty();
        }
        return Optional.of(board);
    }

    public Optional<Board> getBoardByBoardId(Integer boardId){
        return boardMapper.getBoardByBoardId(boardId);
    }

    public List<Board> getBoardList(){
        return boardMapper.getBoardList();
    }
}
