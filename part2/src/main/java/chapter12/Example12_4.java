package chapter12;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * checkpoint(description)을 사용한 디버깅 예
 * - description 을 추가해서 에러가 발생한 지점을 구분할 수 있다.
 * - description 을 지정할 경우 traceback 을 추가하지 않는다.
 */
@Slf4j
public class Example12_4 {
    public static void main(String[] args) {
        Flux
                .just(2, 4, 6, 8) // 2, 4, 6, 8 값을 발행하는 Flux를 생성합니다.
                .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x / y) // 첫 번째 Flux의 값과 두 번째 Flux의 값으로 나눗셈을 수행하여 결합합니다. (주의: 0으로 나눌 수 없으므로 에러가 발생합니다)
                .checkpoint("Example12_4.zipWith.checkpoint") // "Example12_4.zipWith.checkpoint"라는 이름으로 체크포인트를 생성합니다. 이 체크포인트는 연산 체인에서 디버깅 및 에러 추적을 위해 사용됩니다.
                .map(num -> num + 2) // 이전 연산에서 발행된 각 값에 2를 더합니다.
                .checkpoint("Example12_4.map.checkpoint") // "Example12_4.map.checkpoint"라는 이름으로 또 다른 체크포인트를 생성합니다.
                .subscribe(
                        data -> log.info("#onNext: {}", data), // Flux에 구독하고 각 발행 데이터를 "#onNext" 레이블과 함께 로그로 출력합니다.
                        error -> log.error("#onError:", error) // 구독 중 발생하는 모든 에러를 "#onError" 레이블과 함께 로그로 출력합니다.
                );
    }
}

//public class Example12_4 {
//    public static void main(String[] args) {
//        Flux
//            .just(2, 4, 6, 8)
//            .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x/y)
//            .checkpoint("Example12_4.zipWith.checkpoint")
//            .map(num -> num + 2)
//            .checkpoint("Example12_4.map.checkpoint")
//            .subscribe(
//                    data -> log.info("# onNext: {}", data),
//                    error -> log.error("# onError:", error)
//            );
//    }
//}
