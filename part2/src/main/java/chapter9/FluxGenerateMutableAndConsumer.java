package chapter9;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class FluxGenerateMutableAndConsumer { // 구독자를 추가해도 값은 이어지지 않고 초기화
    public static void main(String[] args) {

      // 변경 가능한 상태를 나타내는 AtomicLong 객체를 생성합니다.
        AtomicLong finalState = new AtomicLong(0);

      // Flux.generate 메서드를 사용하여 Flux를 생성합니다.
        Flux<String> flux = Flux.generate(
                // () -> new AtomicLong(),
                // Supplier를 사용하여 초기 상태를 만듭니다.
                AtomicLong::new, // (1) Mutable State variant : state를 변경할 수 있는 객체를 state 값으로 생성
                // (1) 변경 가능한 상태를 나타내는 AtomicLong 객체를 초기 상태로 생성합니다.
                // Generator 함수로 요소를 생성하고 상태를 업데이트합니다.
                (state, sink) -> {
                    long i = state.getAndIncrement(); // (2) 상태 변경 // (2) AtomicLong 상태 값을 가져오고 1 증가시킵니다.
                    sink.next("3 x " + i + " = " + 3*i); // 새로운 요소를 Flux에 발행합니다.
                    if (i == 10) sink.complete(); // i가 10이 되면 Flux를 완료합니다.
                    return state; // (3) 업데이트된 상태를 다음 반복에 사용하기 위해 반환합니다.
                },
                // Flux가 완료된 후 최종 상태를 처리하는 Consumer 함수입니다.
                (state) -> System.out.println("state: " + state));

      // Flux에 구독하고 onNext, onError 및 onComplete 이벤트에 대한 동작을 정의합니다.
        flux.subscribe(data -> log.info("# onNext: {}", data), // 15:58:13.329 [main] INFO - # onNext: 3 x 10 = 30
                // 발행된 데이터를 로그에 남깁니다.
                error -> log.info("# onError: {}", error.getMessage()), // 오류 발생 시 오류 메시지를 로그에 남깁니다.
                () -> log.info("# onComplete")); // 15:58:13.331 [main] INFO - # onComplete
      // Flux가 완료되었을 때 완료 메시지를 로그에 남깁니다.

      // AtomicLong의 최종 값을 출력합니다.
        System.out.println("Final State: " + finalState.get());
    }
}

