package com.study.apiboard.board.service;

import com.study.apiboard.board.entity.Board;
import com.study.apiboard.board.repository.BoardRepository;
import com.study.apiboard.board.dto.BoardRequestDto;
import com.study.apiboard.board.dto.BoardResponseDto;
import com.study.apiboard.exception.CustomException;
import com.study.apiboard.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;
  // boardRepository = JPA Repository 인터페이스
  // 보통 @Autowired 로 빈(Bean)을 주입받는 방식으로 사용했지만,(스프링부트는 생성자로 빈을 주입하는 방식을 권장)
  // 클래스 레벨에 선언된 @RequiredArgsConstructor 는 롬복에서 제공해주는 언테이션으로,
  // 클래스 내에 final 로 선언된 모든 멤버에 대한 생성자를 만들어준다. 아래는 예시
  // public BoardService(BoardRepository boardRepository) {
  //     this.boardRepository = boardRepository;
  // }

  // 게시글 생성
  @Transactional  // Transactional 는 JPA를 사용한다면, 서비스(Service) 클래스에서 필수적으로 사용되어야 하는 어노테이션이다
                  // 일반적으로 메서드 레벨에 선언하게 되며, 메서드의 실행, 종료, 예외를 기준으로 각각 실행(begin), 종료(commit), 예외(rollback)를 자동 처리
  public Long save(final BoardRequestDto params) {
    // save() 메서드
    // boardRepository 의 save() 메서드가 실행되면 새로운 게시글이 생성된다.
    // Entity 클래스는 요청(Request)에 사용되어서는 안 되기 때문에
    // BoardRequestDto의 toEntity() 메서드를 이용하여 boardRepository의 save() 메서드를 실행
    // save() 메서드가 실행된 후 entiry 객체에는 생성된 게시글 정보가 담기며 메서드가 종료되면 생성된 게시글의 id(PK)를 리턴.
    Board entity = boardRepository.save(params.toEntity());
    return entity.getId();
  }

  // 게시글 리스트 조회
  public List<BoardResponseDto> findAll() {
    // findAll() 메서드
    // boardRepository의 findAll() 메서드의 인자로 sort 객체를 전달하여 전체 게시글을 조회
    // sort 객체는 ORDER BY id DESC, created_date DESC 를 의미
    // return 을 보면 Java의 stream API을 사용한것을 확인할수 있는데
    // list변수에는 게시글 Entity 가 담겨있고 각각의 Entity를 BoardResponseDto 타입으로 변경(생성)해서 리턴 해준다고 생각하면 된다
    Sort sort = Sort.by(Sort.Direction.DESC, "id", "createdDate");
    List<Board> list = boardRepository.findAll(sort);
    return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());

    //만약 findAll() 메서드를 Stream API 없이 풀어서 사용한다면 다음과 같을것이다.
    /*
    Sort sort = Sort.by(Direction.DESC, "id", "createdDate");
    List<Board> list = boardRepository.findAll(sort);
    List<BoardResponseDto> boardList = new ArrayList<>();
    for (Board entity : list) {
      boardList.add(new BoardResponseDto(entity));
    }
    return boardList;
    */
    // 내용이 길어지는것을 확인 할 수 있다
    // 게시글 내용을 전부 파악한 후 for 문을 이용하여 게시글의 갯수만큼 boardList 에 담아 노출
  }

  // 게시글 수정
  @Transactional
  public Long update(final Long id, final BoardRequestDto params) {
    Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    entity.update(params.getTitle(), params.getContent(), params.getWriter());
    return id;
  }
}
