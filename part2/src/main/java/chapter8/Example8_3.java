package chapter8;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Unbounded request 일 경우, Downstream 에 Backpressure Drop 전략을 적용하는 예제
 *  - Downstream 으로 전달 할 데이터가 버퍼에 가득 찰 경우, 버퍼 밖에서 대기하는 먼저 emit 된 데이터를 Drop 시키는 전략
 */
@Slf4j
public class Example8_3 {
    public static void main(String[] args) throws InterruptedException {
        Flux
            .interval(Duration.ofMillis(1L)) // 1밀리초마다 순차적으로 증가하는 Long 값을 생성하는 Flux 생성
            .onBackpressureDrop(dropped -> log.info("# dropped: {}", dropped)) // 백프레셔가 발생했을 때 드롭된 요소의 정보를 로그로 출력
            .doOnNext(data -> log.info("$ do OnNext: {} ", data)) // 각 요소가 소비되기 전에 로그로 출력
            .publishOn(Schedulers.parallel()) // 이후의 작업을 병렬 스레드로 실행하도록 스케줄링
            .subscribe(data -> {
                        try {Thread.sleep(5L);} catch (InterruptedException e) {} // 5밀리초 동안 스레드 일시 정지
                        log.info("# onNext: {}", data); // 각 요소를 로그로 출력
                        log.info("-----------------------------------------------------"); // 에러 발생 시 에러 정보를 로그로 출력
                    },
                    error -> log.error("# onError", error)); // 메인 스레드를 2초간 일시 정지

        Thread.sleep(2000L);
    }
}
