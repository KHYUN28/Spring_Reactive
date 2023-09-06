package com.itvillage.reactive.v2.book;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

// Blocking 요소를 제거한 BookController
// DTO 클래스의 객체를 엔티티 클래스의 객체로 변환히는 시점에
// 발생 할 수 있는 Blocking 요소를 제거 한 BookControlkr(V2)
@RestController("bookControllerV2")
@RequestMapping("/v2/books")
public class BookController {
    private final BookService bookService;
    private final BookMapper mapper;

    public BookController(BookService bookService, BookMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    // postBook()， patchBook(), getBook() 핸들러 메서드
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono postBook(@RequestBody Mono<BookDto.Post> requestBody) {
        Mono<Book> result = bookService.createBook(requestBody);

        return result.flatMap(book -> Mono.just(mapper.bookToResponse(book)));
    }

    @PatchMapping("/{book-id}")
    public Mono patchBook(@PathVariable("book-id") long bookId,
                                    @RequestBody Mono<BookDto.Patch> requestBody) {
        Mono<Book> result = bookService.updateBook(bookId, requestBody);
        return result.flatMap(book -> Mono.just(mapper.bookToResponse(book)));
    }

    @GetMapping("/{book-id}")
    public Mono getBook(@PathVariable("book-id") long bookId) {
        return bookService.findBook(bookId)
                .flatMap(book -> Mono.just(mapper.bookToResponse(book)));
        // 서비스 계층의 findBook() 메서드로부터 리턴 값으로 전달받은 Mono〈Book>을 이용해
        // flatMap() Operator 내부에서 다시 DTO 클래스의 객체로 변환함으로써 Blocking 요소를 제거
    }
}
//    BookService.java의 findBook
//    public Mono<Book> findBook(long bookId) {
//        return Mono.just(
//                new Book(bookId,
//                        "Java 고급",
//                        "Advanced Java",
//                        "Kevin",
//                        "111-11-1111-111-1",
//                        "Java 중급 프로그래밍 마스터",
//                        "2022-03-22",
//                        LocalDateTime.now(),
//                        LocalDateTime.now())
//        );
//    }

// @ResponseBody메서드의 주석을 사용하여 HttpMessageWriter 를 통해 응답 본문에 반환을 직렬화할 수 있습니다.
// WebFlux는 단일 값 반응 유형을ResponseEntity 사용하여 본문에 대한 비동기식 및/또는 단일 및 다중 값 반응 유형을 생성하도록 지원


// Blocking I/O (Input/Output)는 컴퓨터 프로그래밍 및 시스템 디자인에서 사용되는 I/O 작업 처리 방식 중 하나입니다.
// 이 방식은 데이터를 읽거나 쓸 때 일반적으로 다음 작업이 완료될 때까지 대기하며, 작업이 완료되면 결과를 반환하는 방식으로 동작합니다.
// 이것은 I/O 작업이 완료될 때까지 다른 작업을 수행하지 못하도록 하는 특징을 갖고 있습니다.

// 큐는 데이터를 저장하는 컨테이너