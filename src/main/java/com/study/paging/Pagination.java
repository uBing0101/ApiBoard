package com.study.paging;

import lombok.Getter;

@Getter
public class Pagination {

  private int totalRecordCount;   // 전체 데이터 수
  // 전체 게시글 개수 를 의미
  // 테이블에 1,000개의 레코드가 있다고 가정할 시
  // 검색 조건이 없는 경우에는 전체 데이터 개수가 되고,
  // 검색 조건이 있는 경우에는 조건에 해당하는 데이터 개수
  private int totalPageCount;     // 전체 페이지 수
  // 페이지 하단에 출력할 전체 페이지 개수
  // 테이블에 1,000개의 레코드가 있고, 페이지당 출력할 데이터 개수가 10개 라고 가정한다면
  // (1,000 / 10) 의 결과인 100이 된다
  private int startPage;          // 첫 페이지 번호
  // 페이지 하단에 출력할 페이지 수(pageSize)가 10 이라면
  // 현재 페이지 번호(page)가 5라고 가정했을 때 1을 의미
  // 페이지 번호가 15라면, startPage는 11이 된다
  private int endPage;            // 끝 페이지 번호
  // 페이지 하단에 출력할 페이지 수(pageSize)가 10 이라면
  // 현재 페이지 번호(page)가 5라고 가정했을 때 10을 의미
  // 페이지 번호가 15라면, endPage는 20이 된다
  private int limitStart;         // LIMIT 시작 위치
  // MySQL의 LIMIT 구문에 사용되는 멤버 변수
  // LIMIT의 첫 번째 파라미터에는 시작 위치, 즉 몇 번째 데이터부터 조회할지를 지정
  // 두번째 파라미터에는 시작 limitStart를 기준으로 조회할 데이터의 개수를 지정
  // 현재 페이지 번호가 1이고, 페이지당 출력할 데이터 개수가 10이라고 가정했을 때
  // (1 - 1) * 10 = 0 이라는 결과가 나오게 되고, LIMIT 0,10으로 쿼리가 실행
  // 페이지 번호가 5라면, LIMIT 40 , 10 으로 쿼리가 실행
  private boolean existPrevPage;  // 이전 페이지 존재 여부
  // 이전 페이지의 존재 여부를 확인하는 데 사용
  // startPage가 1이 아니라면 이전 페이지는 무조건적으로 존재
  private boolean existNextPage;  // 다음 페이지 존재 여부
  // 다음 페이지의 존재 여부를 확인하는 데 사용되는 멤버 변수
  // 페이지당 출력할 데이터 개수가 10개, 끝 페이지 번호가 10 이라고 가정
  // (10 * 10) = 100
  // 전체 데이터 개수가 105개 라면, 다음 페이지 존재 여부는 true 가 된다

  public Pagination(int totalRecordCount, CommonParams params) {
    if(totalRecordCount > 0) {
      this.totalRecordCount = totalRecordCount;
      this.calculation(params);
    }
  }

  private void calculation(CommonParams params) {

    // 전체 페이지 수 계산
    totalPageCount = ((totalRecordCount - 1) / params.getRecordPerPage()) + 1;

    // 현재 페이지 번호가 전체 페이지 수 보다 큰 경우, 현재 페이지 번호에 전체 패이지 수 저장
    if(params.getPage() > totalPageCount) {
      params.setPage(totalPageCount);
    }

    // 첫 페이지 번호 계산
    startPage = ((params.getPage() - 1) / params.getPageSize()) * params.getPageSize() + 1;

    // 끝 페이지 번호 계산
    endPage = startPage + params.getPageSize() -1;

    // 끝 페이지가 전체 페이지 수보다 큰 경우, 끝 페이지 전체 페이지 수 저장
    if(endPage > totalPageCount) {
      endPage = totalPageCount;
    }

    // LIMIT 시작 위치 계산
    limitStart = (params.getPage() - 1) * params.getRecordPerPage();

    // 이전 페이지 존재 여부 확인
    existPrevPage = startPage != 1;

    // 다음 페이지 존재 여부 확인
    existNextPage = (endPage * params.getRecordPerPage()) < totalRecordCount;

  }
}
