package chapter9;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

/**
 * Sinks.Many 예제
 *  - multicast()를 사용해서 하나 이상의 Subscriber에게 데이터를 emit하는 예제
 */
@Slf4j
public class Example9_9 {
    public static void main(String[] args) {
        // Step 1: Sinks.Many를 사용하여 multicastSink 생성
        Sinks.Many<Integer> multicastSink =
                Sinks.many().multicast().onBackpressureBuffer();

        // Step 2: multicastSink를 Flux로 변환
        Flux<Integer> fluxView = multicastSink.asFlux();

        // Step 3: multicastSink를 사용하여 데이터를 방출합니다.
        // FAIL_FAST 옵션: 버퍼가 가득 찼을 때 예외를 발생시키도록 설정
        multicastSink.emitNext(1, FAIL_FAST);
        multicastSink.emitNext(2, FAIL_FAST);

        // Step 4: fluxView를 구독하고 데이터를 처리하는 두 개의 Subscriber를 등록합니다.
        fluxView.subscribe(data -> log.info("# Subscriber1: {}", data));
        fluxView.subscribe(data -> log.info("# Subscriber2: {}", data));

        // Step 5: multicastSink를 사용하여 추가 데이터를 방출합니다.
        multicastSink.emitNext(3, FAIL_FAST);
    }
}

//public class Example9_9 {
//    public static void main(String[] args) {
//        Sinks.Many<Integer> multicastSink =
//                Sinks.many().multicast().onBackpressureBuffer();
//        Flux<Integer> fluxView = multicastSink.asFlux();
//
//        multicastSink.emitNext(1, FAIL_FAST);
//        multicastSink.emitNext(2, FAIL_FAST);
//
//        fluxView.subscribe(data -> log.info("# Subscriber1: {}", data));
//        fluxView.subscribe(data -> log.info("# Subscriber2: {}", data));
//
//        multicastSink.emitNext(3, FAIL_FAST);
//    }
//}
