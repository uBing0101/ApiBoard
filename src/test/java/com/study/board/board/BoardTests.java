package com.study.board.board;

import com.study.board.entity.Board;
import com.study.board.entity.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest // 기존 스프링 레거시 프로젝트와는 달리 스프링부트는 해당 어노테이션만 선언하면 테스팅이 가능하다
//@Transactional  // 테스트 메서드에 @Transactional을 사용하면 트랜잭션으로 감싸지며, 메서드가 종료될 때 자동으로 롤백된다.
class BoardTests {

  @Autowired
  BoardRepository boardRepository;  // 스프링 컨테이너에 등록된 BoardRepository 객체(Bean)를 주입받는다

  @Test
  // save() 게시글 저장에 이용되는 params
  // 빌더(Builder) 패턴을 통해 생성된 객체
  // 생성자와 달리 빌더 패턴을 이용하면 어떤 멤버에 어떤 값을 세팅하는지 직관적으로 확인이 가능하다

  // 생성자를 통해 객체를 생성한 코드 ex)
  // Board entity = new Board("1번 게시글 제목", "1번 게시글 내용", "도뎡이", 0, 'N');
  // 생성자의 경우에는 객체를 생성할 때 인자의 순서에 영향을 받지만
  // 빌더 패턴은 다음과 같이 인자의 순서에 관계없이 객체를 생성할 수 있다
  void save() {
    // 1. 게시글 파라미터 생성
    Board params = Board.builder()
        .title("10번 게시글 제목")
        .content("10번 게시글 내용")
        .writer("작성자")
        .hits(0)
        .deleteYn('N')
        .build();

    // 2. 게시글 저장
    boardRepository.save(params);

    // 3. 1번 게시글 정보 조회
    Board entity = boardRepository.findById((long) 10).get();
    assertThat(entity.getTitle()).isEqualTo("10번 게시글 제목");
    assertThat(entity.getContent()).isEqualTo("10번 게시글 내용");
    assertThat(entity.getWriter()).isEqualTo("작성자");
  }

  @Test
  // findAll()
  // boardRepository 의 count()아 findAll() 메서드를 이용해서
  // 전체 게시글 수 와 전체 게시글 리스트를 조회하는 쿼리를 실행
  void findAll() {
    // 1. 전체 게시글 수 조회
    long boardCount = boardRepository.count();

    // 2. 전체 게시글 리스트 조회
    List<Board> boards = boardRepository.findAll();
  }

  @Test
  // delete()
  // boardRepository 의 findById() 메서드를 이용하여 Entity 를 조회
  // findById() 도 마찬가지로 JPA에서 기본으로 제공해주는 메서드로, 엔티티의 PK를 기준으로 데이터를 조회한 다음 delete() 메서드를 실행해 5번 게시글을 삭제
  // 참고를 하자면 findById()의 리턴 타입은 Optional<T> 라는 클래스 이며
  // 옵셔널은 반복적인 NULL 처리를 피하기 위해 자바8에서 최초로 도입된 클래스
  // Java Optional 키워드로 구글링해서 자료를 찾아볼 수 있다
  void delete() {
    // 1. 게시글 조회
    Board entity = boardRepository.findById((long) 10).get();

    // 2. 게시글 삭제
    boardRepository.delete(entity);

  }

}