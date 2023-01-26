package com.study.board.model;

import com.study.board.entity.Board;
import com.study.paging.CommonParams;
import com.study.paging.Pagination;
import com.study.board.entity.BoardRepository;
import com.study.board.dto.BoardRequestDto;
import com.study.board.dto.BoardResponseDto;
import com.study.exception.CustomException;
import com.study.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private final BoardMapper boardMapper;

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

  // 게시글 수정
  @Transactional
  public Long update(final Long id, final BoardRequestDto params) {
    // update() 메서드
    // 중요하니 기억해둘것!
    // 해당 메서드에는 update 쿼리를 실행하는 로직이 없음.
    // 하지만 해당 메서드의 실행이 종료(commit) 되면 update 쿼리가 자동 실행
    // JPA에는 영속성 컨텍스트 라는 개념이 있는데
    // Entity를 영구히 저장하는 환경이라는 뜻 이며
    // 애플리케이션과 데이터베이스 사이에서 객체를 보관하는 가상의 영역
    // JPA의 엔티티 매니저(Entity Manager) 라는 녀석은 Entity가 생성되거나 조회를 하는 시점에 영속성 컨텍스트에 Entity 를 보관 및 관리함
    // Entity 를 조회 -> Entity 가 영속성 컨텍스트에 보관(포함) -> 영속성 컨텍스트에 포함된 Entity 객체의 값이 변경되면
    // 트랜댁션(Transaction)이 종료(commit) 시점에 update 쿼리를 실행
    Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    entity.update(params.getTitle(), params.getContent(), params.getWriter());
    return id;
  }

  // 게시글 삭제
  @Transactional
  public Long delete(final Long id) {
    Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    entity.delete();
    return id;
  }

  /**
   * 게시글 리스트 조회
   */
  public List<BoardResponseDto> findAll() {
    Sort sort = Sort.by(Direction.DESC, "id", "createdDate");
    List<Board> list = boardRepository.findAll(sort);
    return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());
  }

  // 게시글 리스트 조회 - (삭제 여부 기준)
  public List<BoardResponseDto> findAllByDeleteYn(final char deleteYn) {
    // findAll() 메서드
    // boardRepository의 findAll() 메서드의 인자로 sort 객체를 전달하여 전체 게시글을 조회
    // sort 객체는 ORDER BY id DESC, created_date DESC 를 의미
    // return 을 보면 Java의 stream API을 사용한것을 확인할수 있는데
    // list변수에는 게시글 Entity 가 담겨있고 각각의 Entity를 BoardResponseDto 타입으로 변경(생성)해서 리턴 해준다고 생각하면 된다
    Sort sort = Sort.by(Direction.DESC, "id", "createdDate");
    List<Board> list = boardRepository.findAllByDeleteYn(deleteYn, sort);
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

  // 게시글 리스트 조회  - (With. pagination information)
  public Map<String, Object> findAll(CommonParams params) {

    //게시글 수 조회
    int count = boardMapper.count(params);

    // 등록된 게시글이 없는 경우, 로직 종료
    if(count < 1) {
      return Collections.emptyMap();
    }
    // 검색 조건의 유무에 따라, 테이블에서 게시글 개수를 카운팅
    // 만약 데이터가 하나도 없는 경우에는 NULL이 아닌, 비어있는 Map을 리턴
    // 응답(Response)으로 NULL을 내려주는 습관은 좋지 않다

    // 페이지네이션 정보 계산
    Pagination pagination = new Pagination(count, params);
    params.setPagination(pagination);
    // Pagination 클래스의 생성자는 전체 데이터 수(totalRecordCount),
    // 앞에서 생성한 페이징(검색) 처리용 클래스(CommonParams)를 필요로 한다
    // 데이터가 있는 상황에서 Pagination의 calculation 메서드가 실행되면
    // 페이지네이션 정보다 LIMIT구문에 사용되는 limitStart의 값이 계산되고
    // 계산된 정보는 pagination 객체에 담긴다 그리고 BoardMapper.xml에서 findAll 쿼리 마지막 부분에
    // LIMIT 구문을 보면 첫번째 인자로는 Pagination 의 limitStart를
    // 두번째 인자로는 recordPerPage 를 전달하고 있는데
    // 게시글 데이터를 조회하기 전에 페이지네이션 정보를 계산하는 이유가 이것에 있다
    // 페이지네이션은 전체 데이터 개수를 기준으로 계산
    // limitStart의 값이 매겨지지 않은 상황에서 게시글을 조회한다고 하면
    // LIMIT0, #{recordPerPage}과 같은 쿼리가 계속해서 실행
    // 즉, 계산 이전에 쿼리가 실행된다면 계속해서 첫 번째 페이지인 1페이지만 보이게 될것
    // 그리고 BoardMapper.xml의 findAll은 CommonParams를 파라미터로 전달받기 때문에
    // CommonParams는 Pagination 클래스를 멤버 변수로 가지고 있어야 한다
    // CommonParams 가 Pagination을 멤버로 가지도록 해주면 된다

    // 게시글 리스트 조회
    List<BoardResponseDto> list = boardMapper.findAll(params);

    // 데이터 반환
    Map<String, Object> response = new HashMap<>();
    response.put("params", params);
    response.put("list", list);
    return response;

  }

  // 게시글 상세 조회
  @Transactional
  public BoardResponseDto findById(final Long id) {
    Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    entity.increaseHits();
    return new BoardResponseDto(entity);
  }

}
