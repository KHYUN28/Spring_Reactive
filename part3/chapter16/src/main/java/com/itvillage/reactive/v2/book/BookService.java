package com.itvillage.reactive.v2.book;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service("bookServiceV2")
public class BookService {
    private final BookMapper mapper;

    public BookService(BookMapper mapper) {
        this.mapper = mapper;
    }

    public Mono<Book> createBook(Mono<BookDto.Post> book) {
        // not implement business logic;
        return book.flatMap(post -> Mono.just(mapper.bookPostToBook(post)));
    }

    public Mono<Book> updateBook(final long bookId, Mono<BookDto.Patch> book) {
        // not implement business logic;
        return book.flatMap(patch -> {
            patch.setBookId(bookId);
            return Mono.just(mapper.bookPatchToBook(patch));
        });
    }

    // createBook(), updateBook()의 경우，아직 데이터 액세스 계층과 연동하지 않기 때문에
    // Controller에서 전달받은 Mono 내부에서 Mapper를 이용해 DTO 클래스의 객체를 엔티티 클래스 객체로
    // 변환한 후，Comroller로 되돌려주고 있습니다.

    public Mono<Book> findBook(long bookId) {
        return Mono.just(
                    new Book(bookId,
                            "Java 고급",
                            "Advanced Java",
                            "Kevin",
                            "111-11-1111-111-1",
                            "Java 중급 프로그래밍 마스터",
                            "2022-03-22",
                            LocalDateTime.now(),
                            LocalDateTime.now())
        );
    }
}
