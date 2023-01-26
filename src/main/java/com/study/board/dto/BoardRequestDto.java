package com.study.board.dto;

import com.study.board.entity.Board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDto {

  private String title; // 제목
  private String content; // 내용
  private String writer; // 작성자
  private char deleteYn; // 삭제 여부

  // Entity 객체를 인자로 전달해서 게시글을 생성하는데
  // Entity 클래스는 절대 요청(Request)에 사용되어서는 안되므로
  // BoardRequdstDto로 전달받은 데이터(파라미터)를 기준으로 Entity 객체를 생성한다
  public Board toEntity() {
    return Board.builder()
        .title(title)
        .content(content)
        .writer(writer)
        .hits(0)
        .deleteYn(deleteYn)
        .build();
  }

  // Entity 클래스와 요청 DTO 클래스는 유사한 구조이다
  // Entity 클래스는 테이블 또는 레코드 역할을 하는 데이터베이스 그 자체로 생각 할 수 있고 ,
  // 절대로 요청(Request) 이나 응답(Response)에 사용되어서는 안 되기 때문에 반드시 Request, Response 클래스를 따로 생성 해 주어야 한다.

}
