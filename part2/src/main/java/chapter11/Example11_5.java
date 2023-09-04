package chapter11;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Context의 특징 예제
 *  - Context는 각각의 구독을 통해 Reactor Sequence에 연결 되며 체인의 각 Operator는 연결된 Context에 접근할 수 있어야 한다.
 */
@Slf4j
public class Example11_5 {
    public static void main(String[] args) throws InterruptedException {
        final String key1 = "company"; // 키1: 회사 이름

        Mono<String> mono = Mono.deferContextual(ctx -> // 콘텍스트를 사용하는 Mono를 생성
                        Mono.just("Company: " + " " + ctx.get(key1)) // "Company:" 문자열과 콘텍스트에서 가져온 "company" 값을 결합하여 데이터 생성
                )
                .publishOn(Schedulers.parallel()); // 병렬 스레드 풀에서 Mono의 연산 실행 지정

        mono.contextWrite(context -> context.put(key1, "Apple")) // 첫 번째 Subscriber를 위한 콘텍스트에 "Apple" 값을 추가
                .subscribe(data -> log.info("# subscribe1 onNext: {}", data)); // 첫 번째 Subscriber가 생성된 콘텍스트로 데이터를 구독하고 출력

        mono.contextWrite(context -> context.put(key1, "Microsoft")) // 두 번째 Subscriber를 위한 콘텍스트에 "Microsoft" 값을 추가
                .subscribe(data -> log.info("# subscribe2 onNext: {}", data)); // 두 번째 Subscriber가 생성된 콘텍스트로 데이터를 구독하고 출력

        Thread.sleep(100L); // 메인 스레드를 100밀리초 동안 대기
    }
}

//public class Example11_5 {
//    public static void main(String[] args) throws InterruptedException {
//        final String key1 = "company";
//
//        Mono<String> mono = Mono.deferContextual(ctx ->
//                        Mono.just("Company: " + " " + ctx.get(key1))
//                )
//                .publishOn(Schedulers.parallel());
//
//
//        mono.contextWrite(context -> context.put(key1, "Apple"))
//                .subscribe(data -> log.info("# subscribe1 onNext: {}", data));
//
//        mono.contextWrite(context -> context.put(key1, "Microsoft"))
//                .subscribe(data -> log.info("# subscribe2 onNext: {}", data)); // Subscriber이 2명이랑 Context도 2개 만들어짐
//
//        Thread.sleep(100L);
//    }
//}
