package chapter9;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

/**
 * Sinks.Many 예제
 *  - replay()를 사용하여 이미 emit된 데이터 중에서 특정 개수의 최신 데이터만 전달하는 예제
 */

@SuppressWarnings("ALL")
@Slf4j
public class Example9_10 {
    public static void main(String[] args) {

        // 단계 1: Reactor의 Sinks.Many를 사용하여 replaySink를 생성합니다.
        // - `Sinks.many()`는 새로운 유니캐스트 Sinks.Many를 생성합니다.
        // - `.replay()`를 사용하여 replay sink로 만듭니다. 이는 새로운 구독자에게 지정된 수의 항목을 다시 재생할 수 있습니다.
        // - `.limit(2)`는 재생 제한을 2로 설정하며, 이는 마지막 2개의 발행 항목을 재생합니다.
        Sinks.Many<Integer> replaySink = Sinks.many().replay().limit(2); //replay : Sinks.ManySpec

        // 단계 2: replaySink를 Flux로 변환하여 구독자가 데이터를 소비할 수 있도록 합니다.
        Flux<Integer> fluxView = replaySink.asFlux();

        // 단계 3: replaySink에 데이터를 발행합니다.
        // - `.emitNext(1, FAIL_FAST)`는 값 1을 replaySink에 발행합니다.
        // - `.emitNext(2, FAIL_FAST)`는 값 2를 replaySink에 발행합니다.
        // - `.emitNext(3, FAIL_FAST)`는 값 3을 replaySink에 발행하지만, 제한 때문에 재생되지 않습니다.
        replaySink.emitNext(1, FAIL_FAST);
        replaySink.emitNext(2, FAIL_FAST);
        replaySink.emitNext(3, FAIL_FAST);

        // 단계 4: 첫 번째 구독자 (Subscriber1)로 fluxView에 구독합니다.
        fluxView.subscribe(data -> log.info("# Subscriber1: {}", data));

        // 단계 5: replaySink에 더 많은 데이터를 발행발행합니다.
        // - `.emitNext(4, FAIL_FAST)`는 값 4를 replaySink에 발행합니다.
        // - 제한이 2이므로 가장 오래된 값인 1이 재생 버퍼에서 제거되고 4가 추가됩니다.
        replaySink.emitNext(4, FAIL_FAST);

        // 단계 6: 두 번째 구독자 (Subscriber2)로 fluxView에 구독합니다.
        // - Subscriber2는 마지막으로 발행된 2와 4를 받게 됩니다.
        fluxView.subscribe(data -> log.info("# Subscriber2: {}", data));
    }
}

//@Slf4j
//public class Example9_10 {
//    public static void main(String[] args) {
//        Sinks.Many<Integer> replaySink = Sinks.many().replay().limit(2);
//        Flux<Integer> fluxView = replaySink.asFlux();
//
//        replaySink.emitNext(1, FAIL_FAST);
//        replaySink.emitNext(2, FAIL_FAST);
//        replaySink.emitNext(3, FAIL_FAST);
//
//        fluxView.subscribe(data -> log.info("# Subscriber1: {}", data));
//
//        replaySink.emitNext(4, FAIL_FAST);
//
//        fluxView.subscribe(data -> log.info("# Subscriber2: {}", data));
//    }
//}
