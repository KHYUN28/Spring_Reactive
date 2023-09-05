package chapter14.operator_7_split;

import chapter14.SampleData;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * split 예제
 *  - groupBy(keyMapper, valueMapper) Operator
 *      - emit되는 데이터를 key를 기준으로 그룹화 한 GroupedFlux를 리턴한다.
 *      - 그룹화 된 GroupedFlux로 그룹별 작업을 할 수 있다.
 *      - valueMapper를 추가로 전달해서 그룹화 되어 emit되는 데이터의 값을 미리 다른 값으로 변경할 수 있다.
 */
@Slf4j
public class Example14_58 {
    public static void main(String[] args) {
        // SampleData.books에서 Flux를 생성합니다. SampleData.books는 책 정보를 포함한 리스트입니다.
        Flux.fromIterable(SampleData.books)
                .groupBy(book -> book.getAuthorName(), // 작가 이름을 기준으로 그룹화합니다.
                        book -> book.getBookName() + "(" + book.getAuthorName() + ")") // 그룹화 키를 설정합니다.
                .flatMap(groupedFlux -> groupedFlux.collectList()) // 각 그룹의 책을 리스트로 모읍니다.
                .subscribe(bookByAuthor ->
                        log.info("# book by author: {}", bookByAuthor)); // 결과를 로그에 출력합니다.
    }
}

//Example14_57와 Example14_58 코드는 비슷한 기능을 수행하지만 몇 가지 차이점이 있습니다. 이 코드의 주요 차이점은 다음과 같습니다.
//
//그룹화 키 설정 방법:
//
//Example14_57 코드에서는 groupBy 함수에서 작가 이름을 기준으로 그룹화하고 있습니다.
//Example14_58 코드에서는 groupBy 함수에서 작가 이름을 그룹화 키로 사용하면서, 두 번째 매개변수로 그룹화된 요소들의 표현을 설정하고 있습니다. 이렇게 하면 그룹화된 각 항목이 문자열로 표현되며, 작가 이름과 책 이름이 결합된 형태로 그룹화 키로 사용됩니다.

//flatMap 함수 사용 방법:
//
//Example14_57 코드에서는 flatMap 함수 내에서 groupedFlux.map(...).collectList()를 사용하여 각 그룹의 요소를 매핑하고 리스트로 수집하고 있습니다.
//Example14_58 코드에서는 flatMap 함수 내에서 groupedFlux.collectList()만 사용하여 그룹화된 요소를 리스트로 바로 수집하고 있습니다.
// 이로 인해 각 작가별로 그룹화된 요소가 바로 리스트 형태로 반환됩니다.

//즉, Example14_57 코드에서는 그룹화된 책 목록을 문자열로 변환한 다음 리스트로 수집하고, Example14_58 코드에서는 바로 그룹화된 책 목록을 리스트로 수집합니다.
// 이 차이는 그룹화된 데이터를 처리하는 방식의 차이를 나타냅니다. 결과적으로 두 코드 모두 작가별로 책 목록을 출력하는 것이 목표이지만, 코드의 구현 방식이 약간 다릅니다.

//--------------------------------------------------------------------------------------------------------------------------------------------------------
//public class Example14_58 {
//    public static void main(String[] args) {
//        Flux.fromIterable(SampleData.books)
//                .groupBy(book -> book.getAuthorName(),
//                        book -> book.getBookName() + "(" + book.getAuthorName() + ")")
//                .flatMap(groupedFlux -> groupedFlux.collectList())
//                .subscribe(bookByAuthor ->
//                        log.info("# book by author: {}", bookByAuthor));
//    }
//}