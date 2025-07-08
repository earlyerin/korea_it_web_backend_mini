package com.koreait.BoardStudy.repository;

import com.koreait.BoardStudy.dto.board.AddBoardReqDto;
import com.koreait.BoardStudy.entity.Board;
import com.koreait.BoardStudy.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BoardRepository {
    @Autowired
    private BoardMapper boardMapper;
    public int addBoard(Board board){
        return boardMapper.addBoard(board);
    }

    public Optional<Board> getBoardByBoardId(Integer boardId){
        return boardMapper.getBoardByBoardId(boardId);
    }
}
