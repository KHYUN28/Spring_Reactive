package chapter14.operator_1_create;

import chapter14.SampleData;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * range 예제
 */
@Slf4j
public class Example14_5 {
    public static void main(String[] args) {
        // Flux를 사용하여 데이터 스트림을 생성합니다.
        // range(7, 5)는 7부터 시작하여 5개의 연속적인 정수를 생성합니다.
        Flux
                .range(7, 5)

                // map 함수를 사용하여 각 정수를 인덱스로 활용해 SampleData.btcTopPricesPerYear에서 데이터를 가져옵니다.
                .map(idx -> SampleData.btcTopPricesPerYear.get(idx))

                // subscribe를 호출하여 Flux를 구독합니다.
                // tuple은 Flux에서 발생하는 각 이벤트(데이터)를 나타냅니다.
                .subscribe(tuple -> log.info("{}'s {}", tuple.getT1(), tuple.getT2()));
    }
}

//public class Example14_5 {
//    public static void main(String[] args) {
//        Flux
//            .range(7, 5)
//            .map(idx -> SampleData.btcTopPricesPerYear.get(idx))
//            .subscribe(tuple -> log.info("{}'s {}", tuple.getT1(), tuple.getT2()));
//    }
//}
