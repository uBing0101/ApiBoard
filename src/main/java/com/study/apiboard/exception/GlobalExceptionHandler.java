package com.study.apiboard.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 스프링 예외처리를 위해 @ControllerAdvice 와 @ExceptionHandler 등의 기능을 지원
                      // @ControllerAdvice 는 컨트롤러 전역에서 발생할 수 있는 예외를 잡아 Throw 해주고
                      // @ExceptionHandler 는 특정 클래스에서 발생할 수 있는 예외를 잡아 Throw 해준다
                      // 일반적으로 @ExceptionHandler 는 @ControllerAdvice 가 선언된 클래스에 포함된 메서드에 선언
                      // 이 페이지에 대한 예외 처리는 의미가 없기에 @RestControllerAdvice 를 선언 했으며
                      // 해당 어노테이션 @ControllerAdvice에 @ResponseBody가 적용된 형태로 이해하면 될것이다
@Slf4j  // 롬복에서 제공해주는 기능으로 해당 어노테이션이 선언된 클래스에 자동으로 로그 객체를 생성
        // 코드에서 보이는 것 처럼 log.error(), log.debug()와 같이 로깅 관련 메서드를 사용할 수 있다
public class GlobalExceptionHandler {
//  @ExceptionHandler(RuntimeException.class) // 속성으로는 RuntimeException.class 를 지정했으며
//                                            // BoardApiController의 test() 메서드를 보면 RuntimeException을 throw 하고 있다
//  public String handleRuntimeException(final RuntimeException e) {
//    log.error("handleRuntimeException : {}", e.getMessage());
//    return e.getMessage();
//  }

//  Developer Custom Exception
  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e) {
    log.error("handleCustomException: {}", e.getErrorCode());
    return ResponseEntity.status(e.getErrorCode().getStatus().value()).body(new ErrorResponse(e.getErrorCode()));
  }

//  HTTP 405 Exception
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
    log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage());
    return ResponseEntity.status(ErrorCode.METHOD_NOT_ALLOWED.getStatus().value()).body(new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED));
  }

//  HTTP 500 Exception
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(final Exception e) {
    log.error("handleException: {}", e.getMessage());
    return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value()).body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
  }

  // ResponseEntity<ErrorResponse>
  // ResponseEntity<T> 는 HTTP Request에 대한 응답 데이터를 포함하는 클래스로, <Type>에 해당하는 데이터와 HTTP 상태 코드를 함께 리턴할 수 있다
  // 예외가 발생했을 때, ErrorResponse 형식으로 예외 정보를 Response 로 내려주게 된다
}
