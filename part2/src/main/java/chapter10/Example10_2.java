package chapter10;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * publishOn() 기본 예제
 *  - Operator 체인에서 Downstream Operator의 실행을 위한 쓰레드를 지정한다.
 */
@Slf4j
public class Example10_2 {
    public static void main(String[] args) throws InterruptedException {
        Flux.fromArray(new Integer[] {1, 3, 5, 7}) //publisher가 onNext를 실행
                .doOnNext(data -> log.info("# doOnNext: {}", data)) // main thread가 애를 실행
                .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
                .publishOn(Schedulers.parallel()) // Schedulers.parallel() 여기에서 생성된 Thread가 onNext를 실행
                .subscribe(data -> log.info("# ------ onNext: {}", data));
                // subscribe가 실행되면 LambdaSubscriber가 클래스가 객체를 생성하고,
                // 데이터 스트림이 해당 구독에 연결

        Thread.sleep(500L);
    }
}