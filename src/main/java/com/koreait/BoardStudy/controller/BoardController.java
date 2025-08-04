package com.koreait.BoardStudy.controller;

import com.koreait.BoardStudy.dto.board.AddBoardReqDto;
import com.koreait.BoardStudy.dto.board.UpdateBoardReqDto;
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

    @PostMapping("/remove/{boardId}")
    public ResponseEntity<?> removeBoardByBoardId(@PathVariable Integer boardId,
                                                  @AuthenticationPrincipal PrincipalUser principalUser){
        return ResponseEntity.ok(boardService.removeBoardByBoardId(boardId, principalUser));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateBoardByBoardId(@RequestBody UpdateBoardReqDto updateBoardReqDto){
        return ResponseEntity.ok((boardService.updateBoardByBoardId(updateBoardReqDto)));
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
