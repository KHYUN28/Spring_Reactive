package chapter14.operator_4_peek;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

/**
 * doOnXXXX 예제
 *  - doOnXXXX() Operator의 호출 시점을 알 수 있다.
 */
@Slf4j
public class Example14_42 {
    public static void main(String[] args) {
        // Flux.range를 사용하여 1부터 5까지의 숫자를 방출하는 Flux를 생성합니다.
        Flux.range(1, 5)
                // doFinally 연산자를 사용하여 Flux가 완료될 때마다 로그를 출력합니다.
                .doFinally(signalType -> log.info("# doFinally 1: {}", signalType))
                .doFinally(signalType -> log.info("# doFinally 2: {}", signalType))
                // doOnNext 연산자를 사용하여 각 요소가 방출될 때마다 로그를 출력합니다.
                .doOnNext(data -> log.info("# range --------> doOnNext(): {}", data))
                // doOnRequest 연산자를 사용하여 요청한 데이터 양을 로그로 출력합니다.
                .doOnRequest(data -> log.info("# doOnRequest: {}", data))
                // doOnSubscribe 연산자를 사용하여 구독이 시작될 때 로그를 출력합니다.
                .doOnSubscribe(subscription -> log.info("# doOnSubscribe 1"))
                // doFirst 연산자를 사용하여 Flux의 첫 번째 동작으로 로그를 출력합니다.

                .doFirst(() -> log.info("# doFirst()"))
                // filter 연산자를 사용하여 홀수만 필터링합니다.
                .filter(num -> num % 2 == 1)
                // 다시 doOnNext 연산자를 사용하여 필터링 후의 요소가 방출될 때마다 로그를 출력합니다.
                .doOnNext(data -> log.info("# filter --------> doOnNext(): {}", data))
                // doOnComplete 연산자를 사용하여 Flux가 완료될 때 로그를 출력합니다.
                .doOnComplete(() -> log.info("# doOnComplete()"))
                // subscribe를 통해 Flux를 구독하고, BaseSubscriber를 사용하여 요소를 처리합니다.
                .subscribe(new BaseSubscriber<>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1); // 구독 시작 시, 첫 번째 데이터를 요청합니다.
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("# hookOnNext: {}", value); request(1); // 각 요소가 방출될 때마다 로그를 출력하고 다음 데이터를 요청합니다.
                    }
                });
    }
}

//public class Example14_42 {
//    public static void main(String[] args) {
//        Flux.range(1, 5)
//            .doFinally(signalType -> log.info("# doFinally 1: {}", signalType))
//            .doFinally(signalType -> log.info("# doFinally 2: {}", signalType))
//            .doOnNext(data -> log.info("# range --------> doOnNext(): {}", data))
//            .doOnRequest(data -> log.info("# doOnRequest: {}", data))
//            .doOnSubscribe(subscription -> log.info("# doOnSubscribe 1"))
//            .doFirst(() -> log.info("# doFirst()"))
//            .filter(num -> num % 2 == 1)
//            .doOnNext(data -> log.info("# filter --------> doOnNext(): {}", data))
//            .doOnComplete(() -> log.info("# doOnComplete()"))
//            .subscribe(new BaseSubscriber<>() {
//                @Override
//                protected void hookOnSubscribe(Subscription subscription) {
//                    request(1);
//                }
//
//                @Override
//                protected void hookOnNext(Integer value) {
//                    log.info("# hookOnNext: {}", value);
//                    request(1);
//                }
//            });
//    }
//}