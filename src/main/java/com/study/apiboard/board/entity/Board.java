package com.study.apiboard.board.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter // Setter 이 없는이유는 Entity 는 테이블 그 자체가 되므로  컬럼에 setter 을 무조건 생성하는 경우 어느시점에 변경되어있는지 알수가 없기에 set메서드가 존재하지 않는댜
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 해당 클래스의 기본 생성자를 생성해주는 어노테이션
                                                    // access 속성을 이용해 동일한 패키지 내의 클래스에서만 객체를 생성할수있도록 제어
@Entity  // 해당 클래스가 테이블과 매핑되는 JPA의 엔티티 클래스 를 의미
        // 기본적으로 클래스명(camel case) 를 테이블명(Snake Case)로 매핑
public class Board {

  @Id // 해당 Entity 가 PK값임을 의미 보통 DB에서는 PK를 bigint타입으로 Entity에서는 Long 타입으로 선언
  @GeneratedValue(strategy = GenerationType.IDENTITY) // PK생성 전략 설정 어노테이션 mysql 등 Auto_increment 를 지원하면 사용
                                                      // 오라클처럼 시퀀스를 설정할경우 GenerationType.SEQUENCE 사용
                                                      // GenerationType.AUTO 으로 설정할 경우 db에서 제공하는 PK의 생성 전략을 가져가게된다
  private Long id; // PK

  private String title; // 제목

  private String content; // 내용

  private String writer; // 작성자

  private int hits; // 조회 수

  private char deleteYn; // 삭제 여부

  private LocalDateTime createdDate = LocalDateTime.now(); // 생성일

  private LocalDateTime modifiedDate; // 수정일

  @Builder  // 롬복에서 제공하는 기능으로 생성자 대신에 이용하는 패턴 (생성자를 대신함)
  public Board(String title, String content, String writer, int hits, char deleteYn) {
    this.title = title;
    this.content = content;
    this.writer = writer;
    this.hits = hits;
    this.deleteYn = deleteYn;
  }

  // 게시글 수정 기능 추가
  public void update(String title, String content, String writer) {
    this.title = title;
    this.content = content;
    this.writer = writer;
    this.modifiedDate = LocalDateTime.now();
  }
}
