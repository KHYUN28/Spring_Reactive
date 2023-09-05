package chapter14.operator_7_split;

import chapter14.SampleData;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * split 예제
 *  - groupBy() Operator
 *      - emit되는 데이터를 key를 기준으로 그룹화 한 GroupedFlux를 리턴한다.
 *      - 그룹화 된 GroupedFlux로 그룹별 작업을 할 수 있다.
 *      - 저자 명으로 된 도서의 가격
 */
@Slf4j
public class Example14_59 {
    public static void main(String[] args) {
        Flux.fromIterable(SampleData.books)
                .groupBy(book -> book.getAuthorName())
                // 작가 이름을 기준으로 그룹화합니다.
                .flatMap(groupedFlux ->
                    Mono
                        .just(groupedFlux.key())//.doOnNext(data -> log.info("key : {}"), data))
                        .zipWith(
                            groupedFlux
                                     // book("Advance Java", "Tom", "Tom-boy",25000,100)
                                     // book("Getting started Java", "tom", "Tom-boy",32000,230)
                                    .doOnNext(book -> log.info("Who : {}", book.getAuthorName()))
                                    .map(book -> (int)(book.getPrice() * book.getStockQuantity() * 0.1))
                                    .doOnNext(data -> log.info("doOnNext:{}", data))
                                .reduce((y1, y2) -> y1 + y2),
                                    (authorName, sumRoyalty) -> authorName + "'s royalty: " + sumRoyalty)
                )
                .doOnNext((data) -> log.info("------------"))
                .subscribe(log::info);
    }
}
//public class Example14_59 {
//    public static void main(String[] args) {
//        // SampleData.books에서 Flux를 생성합니다. SampleData.books는 책 정보를 포함한 리스트입니다.
//        Flux.fromIterable(SampleData.books)
//                .groupBy(book -> book.getAuthorName()) // 작가 이름을 기준으로 그룹화합니다.
//                .flatMap(groupedFlux ->
//                                Mono
//                                        .just(groupedFlux.key()) //tom
//                                            .doOnNext(data -> log.info("key : {}"), data))
//                                        .zipWith(groupedFlux // 작가 이름을 Mono로 감싸서 발행합니다.
//                                        //other
//
//                                                // book("Advance Java", "Tom", "Tom-boy",25000,100)
//                                                // book("Getting started Java", "tom", "Tom-boy",32000,230)
//                                                .doOnNext(book -> log.info("Who : {}", book.getAuthorName()))
//                                                .map(book -> (int)(book.getPrice() * book.getStockQuantity() * 0.1)) // 각 책의 로열티를 계산합니다.
//                                                .doOnNext(data -> log.info("doOnNext:{}", data))
//                                                .reduce((y1, y2) -> y1 + y2), // 로열티를 합산합니다.
//                                        (authorName, sumRoyalty) -> authorName + "'s royalty: " + sumRoyalty) // 작가 이름과 로열티를 조합하여 결과를 생성합니다.
//                )
//                .subscribe(log::info); // 결과를 로그에 출력합니다.
//    }
//}

//public class Example14_59 {
//    public static void main(String[] args) {
//        Flux.fromIterable(SampleData.books)
//                .groupBy(book -> book.getAuthorName())
//                .flatMap(groupedFlux ->
//                    Mono
//                        .just(groupedFlux.key())
//                        .zipWith(
//                            groupedFlux
//                                .map(book ->
//                                    (int)(book.getPrice() * book.getStockQuantity() * 0.1))
//                                .reduce((y1, y2) -> y1 + y2),
//                                    (authorName, sumRoyalty) ->
//                                        authorName + "'s royalty: " + sumRoyalty)
//                )
//                .subscribe(log::info);
//    }
//}