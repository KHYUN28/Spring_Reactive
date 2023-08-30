package chapter8;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/* Unbounded request 일 경우, Downstream 에 Backpressure Error 전략을 적용하는 예제
  - Downstream 으로 전달 할 데이터가 버퍼에 가득 찰 경우, Exception을 발생 시키는 전략*/

@Slf4j
public class Example8_2 {
    public static void main(String[] args) throws InterruptedException {
        // 1. 1밀리초 간격으로 순차적인 Long 값을 방출하는 Flux 생성
        Flux
            .interval(Duration.ofMillis(1L))
            .onBackpressureError() // 2. 배압(backpressure) 에러 발생 시 에러 스트림 생성
            .doOnNext(data -> log.info("# doOnNext: {} [2]", data)) // 3. 각 데이터가 발생할 때마다 로깅, doOnNext: 255까지 보내고 끝나는 이유는 Buffer가 꽉 차서.
            .publishOn(Schedulers.parallel()) // 4. 다음 연산들을 병렬 스레드로 실행하도록 스케줄링
            .subscribe(data -> { // 5. 데이터가 발생할 때마다 처리하는 로직 정의
                        try {
                            log.info("# onNext_try"); // 5.1. 데이터 처리 전 로깅
                            Thread.sleep(5L);  } catch (InterruptedException e) {} // 5.2. 5밀리초 동안 스레드 일시정지

                        log.info("# onNext: {}", data); // 5.3. 데이터 처리 후 로깅
                        log.info("--------------------------------------------------------");
                    },
                    error -> log.error("# onError", error)); // 6. 에러 발생 시 에러 로깅 (onNext 255까지 출력한 다음 에러 발생)

        log.info("#### Thread.sleep.Before");

        Thread.sleep(2000L);  // 7. 메인 스레드를 2초 동안 대기시킴

      log.info("#### Thread.sleep.After");
    }
}