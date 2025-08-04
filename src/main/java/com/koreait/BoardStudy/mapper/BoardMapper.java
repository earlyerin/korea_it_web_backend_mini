package com.koreait.BoardStudy.mapper;

import com.koreait.BoardStudy.entity.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    int addBoard(Board board);
    int removeBoardByBoardId(Integer boardId);
    int updateBoardByBoardId(Board board);
    Optional<Board> getBoardByBoardId(Integer boardId);
    List<Board> getBoardList();
}
