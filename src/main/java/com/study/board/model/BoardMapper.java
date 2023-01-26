package com.study.board.model;

import com.study.board.dto.BoardResponseDto;
import com.study.paging.CommonParams;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
// MyBatis는 @Mapper가 선언된 인터페이스와 연결된 XML Mapper 에서 메서드명과 동일한 SQL을 찾아 쿼리를 실행
public interface BoardMapper {

  // 게시글 수 조회
  int count(final CommonParams params);
  // totalRecordCount와 연관되는 메서드
  // 검색 조건의 유무에 따라, 테이블에서 데이터 수를 카운팅 한다
  // 카운팅 된 데이터 수(totalRecordCount)를 기준으로 페이지 번호를 계산

  // totalRecordCount
  // 전체 게시글의 개수를 의미
  // 예를 들어, 테이블에 1,000개의 레코드가 있다고 가정했을 때
  // 검색 조건이 없는 경우에는 전체 데이터 개수가 되고,
  // 검색 조건이 있는 경우에는 조건에 해당되는 데이터 개수가 된다

  //게시글 리스트 조회
  List<BoardResponseDto> findAll(final CommonParams params);
  // findAll 은 count()와 마찬가지로, 검색 조건의 유무를 기준으로 게시글 데이터를 조회한다

}
