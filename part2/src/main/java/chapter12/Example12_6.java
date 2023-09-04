package chapter12;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * 복잡한 단계를 거치는 Operator 체인에서 checkpoint()를 사용하는 예제
 */
@Slf4j
public class Example12_6 {
    public static void main(String[] args) {
        Flux<Integer> source = Flux.just(2, 4, 6, 8); // 정수 값을 발행하는 Flux 'source'를 생성합니다.
        Flux<Integer> other = Flux.just(1, 2, 3, 0); // 또 다른 정수 값을 발행하는 Flux 'other'를 생성합니다.

        // 'divide' 메서드를 사용하여 'source'와 'other' Flux를 결합하고, 나눗셈 연산을 수행한 결과인 'multiplySource' Flux를 생성합니다.
        Flux<Integer> multiplySource = divide(source, other).checkpoint();

        // 'plus' 메서드를 사용하여 'multiplySource' Flux에 각 값에 2를 더한 결과인 'plusSource' Flux를 생성합니다.
        Flux<Integer> plusSource = plus(multiplySource).checkpoint();

        // 'plusSource' Flux를 구독하고, 각 발행 데이터를 로그로 출력합니다.
        plusSource.subscribe(
                data -> log.info("# onNext: {}", data),
                error -> log.error("# onError:", error)
        );
    }

    private static Flux<Integer> divide(Flux<Integer> source, Flux<Integer> other) {
        return source.zipWith(other, (x, y) -> x / y); // 'source'와 'other' Flux를 결합하고 나눗셈 연산을 수행하여 결과를 발행하는 Flux를 반환합니다.
    }

    private static Flux<Integer> plus(Flux<Integer> source) {
        return source.map(num -> num + 2); // 'source' Flux의 각 값을 2를 더한 결과를 발행하는 Flux를 반환합니다.
    }
}

//public class Example12_6 {
//    public static void main(String[] args) {
//        Flux<Integer> source = Flux.just(2, 4, 6, 8);
//        Flux<Integer> other = Flux.just(1, 2, 3, 0);
//
//        Flux<Integer> multiplySource = divide(source, other).checkpoint();
//        Flux<Integer> plusSource = plus(multiplySource).checkpoint();
//
//
//        plusSource.subscribe(
//                data -> log.info("# onNext: {}", data),
//                error -> log.error("# onError:", error)
//        );
//    }
//
//    private static Flux<Integer> divide(Flux<Integer> source, Flux<Integer> other) {
//        return source.zipWith(other, (x, y) -> x/y);
//    }
//
//    private static Flux<Integer> plus(Flux<Integer> source) {
//        return source.map(num -> num + 2);
//    }
//}
