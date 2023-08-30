package chapter8;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

/**
 * doOnXXXX 예제
 *  - doOnXXXX() Operator의 호출 시점을 알 수 있다.
 */
@Slf4j
public class Example8_1 {
    public static void main(String[] args) {
        Flux.range(1, 5)
            .doOnRequest(data -> log.info("# --------------- doOnRequest: {}", data))
            .subscribe(new BaseSubscriber<Integer>() {
                @Override
                protected void hookOnSubscribe(Subscription subscription) {
                  log.info("@ Start hookOnSubscribe");
                  request(1); // request가 호출되기 직전에 doOnRequest() 실행 [long.MAX.VALUE]
                  log.info("@ End hookOnSubscribe"); // 맨 마지막에 return
                }

                @SneakyThrows
                @Override
                protected void hookOnNext(Integer value) {
                    Thread.sleep(2000L);
                    log.info("# + hookOnNext: {}", value);
                    request(1);
                    log.info("# - hookOnNext: {}", value);
                }
            });
    }
}

