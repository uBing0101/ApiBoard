package com.study.apiboard.board.controller;

import com.study.apiboard.exception.CustomException;
import com.study.apiboard.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BoardApiController {
  @GetMapping("/test")
  public String test() {
//    throw new RuntimeException("Holy! Exception...");
    throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
  }
}
