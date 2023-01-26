package com.study.board.entity;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository 의 경우 MyBatis 의 Mapper 와 유사한 퍼시스턴스 영역으로 Repository로 사용됨
// 레파지토리 인터페이스에서 JpaRepository 인터페이스를 상속받을 때 엔티티 클래스 타입(Board)와 PK에 해당하는 데이터 타입(Long)을 선언하면
// 해당 엔티티 클래스와 매핑되는 테이블인 board 테이블의 CRUD 기능을 사용할수 있음
public interface BoardRepository extends JpaRepository<Board, Long> {

  /*
   * 게시글 리스트 조회 - (삭제 여부 기준)
   */
  List<Board> findAllByDeleteYn(final char deleteYn, final Sort sort);
}
