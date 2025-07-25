package com.koreait.BoardStudy.controller;

import com.koreait.BoardStudy.dto.board.AddBoardReqDto;
import com.koreait.BoardStudy.security.model.PrincipalUser;
import com.koreait.BoardStudy.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
public class BoardController {
    @Autowired
    private BoardService boardService;

    @PostMapping("/add")
    public ResponseEntity<?> addBoard(@RequestBody AddBoardReqDto addBoardReqDto,
                                      @AuthenticationPrincipal PrincipalUser principalUser){
        return ResponseEntity.ok(boardService.addBoard(addBoardReqDto, principalUser));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardByyBoardId(@PathVariable Integer boardId){
        return ResponseEntity.ok(boardService.getBoardByBoardId(boardId));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getBoardList(){
        return ResponseEntity.ok(boardService.getBoardList());
    }
}
