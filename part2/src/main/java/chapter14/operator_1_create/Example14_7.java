package chapter14.operator_1_create;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * defer 예제
 */
@Slf4j
public class Example14_7 { // 이 코드 switchIfEmpty 수정해야함.
    public static void main(String[] args) throws InterruptedException {

        log.info("# start: {}", LocalDateTime.now());// 로그 출력을 위한 시작 시간을 기록

        // "Hello"를 포함한 Mono를 생성하고, 3초 동안 지연(delay)시킵니다.
        Mono
            .just("Hello")
            .delayElement(Duration.ofSeconds(3))

            // Mono가 비어있을 때 실행할 대체 작업을 정의하는 switchIfEmpty를 사용합니다.
            .switchIfEmpty(sayDefault()) // eager evaluation // sayDefault() 애가 호출되면 안되는데 호출이 됨 switchIfEmpty() 메소드가 문제가 있음

            // 생성된 Mono를 구독하고 onNext 이벤트를 처리하여 데이터를 로그에 출력
            .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(3500);// 3.5초 동안 대기 (delayElement 때문에)
    }

    // 비어있는 Mono에 대한 대체 작업을 정의한 메서드
    private static Mono<String> sayDefault() {
        log.info("# Say Hi"); // 로그에 "Say Hi" 출력
        return Mono.just("Hi"); // "Hi"를 포함한 Mono를 반환
    }
}
//public class Example14_7 {
//    public static void main(String[] args) throws InterruptedException {
//        log.info("# start: {}", LocalDateTime.now());
//        Mono
//            .just("Hello")
//            .delayElement(Duration.ofSeconds(3))
//            .switchIfEmpty(sayDefault())
////            .switchIfEmpty(Mono.defer(() -> sayDefault()))
//            .subscribe(data -> log.info("# onNext: {}", data));
//
//        Thread.sleep(3500);
//    }
//
//    private static Mono<String> sayDefault() {
//        log.info("# Say Hi");
//        return Mono.just("Hi");
//    }
//}
