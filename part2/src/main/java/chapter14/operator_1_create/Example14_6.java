package chapter14.operator_1_create;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * defer 예제
 */
@Slf4j
public class Example14_6 {
    public static void main(String[] args) throws InterruptedException {
        // 로그 출력을 위한 시작 시간을 기록
        log.info("# start: {}", LocalDateTime.now());

        // Mono.just를 사용하여 현재 시간을 포함한 Mono를 생성
        Mono<LocalDateTime> justMono = Mono.just(LocalDateTime.now()); // 처음 만들어진 publisher
        // Mono.defer를 사용하여 현재 시간을 포함한 Mono를 생성 (deferred Mono)
        Mono<LocalDateTime> deferMono = Mono.defer(() -> Mono.just(LocalDateTime.now())); // 이 publisher는 구독시점에 만들어짐

        Thread.sleep(2000); // 2초 동안 대기

        // justMono를 구독하고 onNext 이벤트를 처리하여 현재 시간을 로그에 출력
        justMono.subscribe(data -> log.info("# onNext just1: {}", data));
        // deferMono를 구독하고 onNext 이벤트를 처리하여 현재 시간을 로그에 출력
        deferMono.subscribe(data -> log.info("# onNext defer1: {}", data));

        Thread.sleep(2000); // 2초 동안 대기

        // justMono를 다시 구독하고 onNext 이벤트를 처리하여 새로운 현재 시간을 로그에 출력
        justMono.subscribe(data -> log.info("# onNext just2: {}", data));
        // deferMono를 다시 구독하고 onNext 이벤트를 처리하여 새로운 현재 시간을 로그에 출력
        deferMono.subscribe(data -> log.info("# onNext defer2: {}", data));
    }
}

//public class Example14_6 {
//    public static void main(String[] args) throws InterruptedException {
//        log.info("# start: {}", LocalDateTime.now());
//        Mono<LocalDateTime> justMono = Mono.just(LocalDateTime.now());
//        Mono<LocalDateTime> deferMono = Mono.defer(() ->
//                                                    Mono.just(LocalDateTime.now()));
//
//        Thread.sleep(2000);
//
//        justMono.subscribe(data -> log.info("# onNext just1: {}", data));
//        deferMono.subscribe(data -> log.info("# onNext defer1: {}", data));
//
//        Thread.sleep(2000);
//
//        justMono.subscribe(data -> log.info("# onNext just2: {}", data));
//        deferMono.subscribe(data -> log.info("# onNext defer2: {}", data));
//    }
//}
