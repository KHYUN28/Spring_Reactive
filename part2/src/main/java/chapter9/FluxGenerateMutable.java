package chapter9;

import java.util.concurrent.atomic.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxGenerateMutable {
    public static void main(String[] args) {
        Flux<String> flux = Flux.generate(
                AtomicLong::new, // (1) Mutable State variant : state를 변경할 수 있는 객체를 state 값으로 생성
                // AtomicLong 클래스는 증가, 감소, 덧셈, 뺄셈, 설정, 값 가져오기 등의 원자적 연산을 수행하는 메서드를 제공합니다.
                // 가장 일반적인 AtomicLong 사용 사례 중 하나는 여러 스레드 간에 공유하는 카운터나 값 유지에 사용
                (state, sink) -> {
                    long i = state.getAndIncrement(); // (2) 상태 변경
                    // getAndIncrement : 값을 가져와서 증가
                    sink.next("3 x " + i + " = " + 3*i);
                    if (i == 10) sink.complete();
                    return state; // (3)
                });

        flux.subscribe(data -> log.info("# onNext: {}", data), // 15:58:13.329 [main] INFO - # onNext: 3 x 10 = 30
                error -> log.info("# onError: {}", error.getMessage()),
                () -> log.info("# onComplete")); // 15:58:13.331 [main] INFO - # onComplete
    }
}
