package chapter10;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/*Time Slice, 10ms, Thread scheduling*/

//subscribeOn() : 구독이 발생한 직후 실행될 스레드를 지정하는 Operator
//publishOn() : 특정 Thread에서 실행 될 수 있도록 해주는 Operator
//parallel()

// UpStream : Usually Publisher
// DownStream : Usually Subscriber

/**
 * subscribeOn() 기본 예제
 *  - 구독 시점에 Publisher의 실행을 위한 쓰레드를 지정한다
 */
@Slf4j
public class Example10_1 {
    public static void main(String[] args) throws InterruptedException {
        // Flux 클래스를 사용하여 Integer 배열을 기반으로한 Flux 데이터 스트림을 생성합니다.
        // 이 스트림에는 1, 3, 5 및 7과 같은 정수 값이 포함됩니다.
        Flux.fromArray(new Integer[] {1, 3, 5, 7})
                // 이 부분은 데이터 스트림을 별도의 스레드에서 처리하도록 스케줄링합니다.
                // Schedulers.boundedElastic()는 Reactor의 스케줄러 중 하나로, 적절한 스레드 풀을 사용하여 비동기 작업을 처리합니다.
                // 이렇게 하면 데이터 스트림의 처리가 메인 스레드와 별도의 스레드에서 이루어집니다.
                .subscribeOn(Schedulers.boundedElastic()) //boundedElastic : 코에갯수의 x10개를 만들어서 사용가능.
                // .doOnNext 연산자는 데이터 스트림의 각 항목이 통과할 때 호출되는 함수를 지정합니다.
                // 이 경우, 각 데이터 항목이 스트림을 통과할 때마다 해당 항목을 로그로 출력합니다.
                // data는 각 항목의 값입니다.
                .doOnNext(data -> log.info("#----------doOnNext: {}", data))
                // .doOnSubscribe 연산자는 데이터 스트림에 구독(subscription)이 시작될 때 호출되는 함수를 지정합니다.
                // 구독이 시작될 때 "doOnSubscribe"라는 메시지를 로그로 출력합니다.
                .doOnSubscribe(subscription -> log.info("# doOnSubscribe")) // main Thead에서 실행
                // .subscribe 메서드는 데이터 스트림을 구독합니다. 구독되면 각 데이터 항목이 처리될 때 호출되는 함수를 지정합니다.
                // 이 경우, 각 항목을 로그로 출력합니다. data는 각 항목의 값입니다.
                .subscribe(data -> log.info("# onNext: {}", data));
        // 메인 스레드를 0.5초 동안 대기합니다.
        Thread.sleep(500L);
    }
}

//public class Example10_1 {
//    public static void main(String[] args) throws InterruptedException {
//        Flux.fromArray(new Integer[] {1, 3, 5, 7})
//                .subscribeOn(Schedulers.boundedElastic())
//                .doOnNext(data -> log.info("#----------doOnNext: {}", data))
//                .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
//                .subscribe(data -> log.info("# onNext: {}", data));
//
//        Thread.sleep(500L);
//    }
//}
