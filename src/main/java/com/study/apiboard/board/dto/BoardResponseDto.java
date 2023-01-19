package com.study.apiboard.board.dto;

import com.study.apiboard.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
  private Long id; // PK
  private String title; // 제목
  private String content; // 내용
  private String writer; // 작성자
  private int hits; // 조회 수
  private char deleteYn; // 삭제 여부
  private LocalDateTime createdDate; // 생성일
  private LocalDateTime modifiedDate; // 수정일

  public BoardResponseDto(Board entity) {
    this.id = entity.getId();
    this.title = entity.getTitle();
    this.content = entity.getContent();
    this.writer = entity.getWriter();
    this.hits = entity.getHits();
    this.deleteYn = entity.getDeleteYn();
    this.createdDate = entity.getCreatedDate();
    this.modifiedDate = entity.getModifiedDate();
  }

  // 응답(Response)도 마찬가지로 Entity 클래스가 사용되어서는 안 되기에 클래스를 분리해서 사용
  // 응답(Response) 객체 생성은 필수적으로 Entity 클래스를 필요로 한다
}
