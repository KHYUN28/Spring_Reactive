package chapter9;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

/**
 * Sinks.Many 예제
 *  - unicast()통해 단 하나의 Subscriber만 데이터를 전달 받을 수 있다
 */
@Slf4j
public class Example9_8 {
    public static void main(String[] args) throws InterruptedException {

        // Step 1: Sinks.Many를 사용하여 unicastSink 생성
        Sinks.Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer();

        // Step 2: unicastSink를 Flux로 변환
        Flux<Integer> fluxView = unicastSink.asFlux();

        // Step 3: unicastSink를 사용하여 데이터를 방출합니다.
        // FAIL_FAST 옵션: 버퍼가 가득 찼을 때 예외를 발생시키도록 설정
        unicastSink.emitNext(1, FAIL_FAST);
        unicastSink.emitNext(2, FAIL_FAST);

        // Step 4: fluxView를 구독하고 데이터를 처리하는 Subscriber를 등록합니다.
        fluxView.subscribe(data -> log.info("# Subscriber1: {}", data));

        // Step 5: unicastSink를 통해 추가 데이터를 방출합니다.
        unicastSink.emitNext(3, FAIL_FAST);

        // Step 6: 주석 처리된 코드
        // 주석 처리된 코드를 활성화하면 새로운 Subscriber를 추가로 등록합니다.
        // fluxView.subscribe(data -> log.info("# Subscriber2: {}", data));
    }
}

//public class Example9_8 {
//    public static void main(String[] args) throws InterruptedException {
//        Sinks.Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
//        Flux<Integer> fluxView = unicastSink.asFlux();
//
//        unicastSink.emitNext(1, FAIL_FAST);
//        unicastSink.emitNext(2, FAIL_FAST);
//
//
//        fluxView.subscribe(data -> log.info("# Subscriber1: {}", data));
//
//        unicastSink.emitNext(3, FAIL_FAST);
//
////        fluxView.subscribe(data -> log.info("# Subscriber2: {}", data));
//    }
//}
