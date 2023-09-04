package chapter11;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Context 기본 예제
 *  - contextWrite() Operator로 Context에 데이터 쓰기 작업을 할 수 있다.
 *  - Context.put()으로 Context에 데이터를 쓸 수 있다.
 *  - deferContextual() Operator로 Context에 데이터 읽기 작업을 할 수 있다.
 *  - Context.get()으로 Context에서 데이터를 읽을 수 있다.
 *  - transformDeferredContextual() Operator로 Operator 중간에서 Context에 데이터 읽기 작업을 할 수 있다.
 */

// Reactor의 Context는 Operator 같은 Reactor 구성요소 간의 전파되는 key/
// value 형태의 저장소라고 정의하는데, 여기서의 '전파’는 Downstream에서 Upstream으로 Context 전파
@Slf4j
public class Example11_1 {
    public static void main(String[] args) throws InterruptedException {
        Mono
                .deferContextual(ctx -> // 콘텍스트를 사용하는 Mono를 생성
                        Mono
                                .just("Hello" + " " + ctx.get("firstName")) // "Hello"와 콘텍스트에서 가져온 "firstName"을 결합하여 데이터 생성
                                .doOnNext(data -> log.info("# just doOnNext : {}", data)) // 데이터를 받을 때 로그를 출력하고 boundedElastic 스레드에서 실행
                )
//            .doOnNext(data -> log.info("# just doOnNext : {}", data))
                .subscribeOn(Schedulers.boundedElastic()) // boundedElastic 스레드 풀에서 Mono 실행 지정
                .publishOn(Schedulers.parallel()) // 병렬 스레드 풀에서 Mono의 나머지 연산 실행 지정
                .transformDeferredContextual(
                        (mono, ctx) -> mono.map(data -> data + " " + ctx.get("lastName")) // 콘텍스트에서 "lastName" 값을 가져와 데이터에 추가
                )
                .contextWrite(context -> context.put("lastName", "Jobs")) // "lastName" 값을 새로운 콘텍스트에 추가
                .contextWrite(context -> context.put("firstName", "Steve")) // "firstName" 값을 새로운 콘텍스트에 추가
                .subscribe(data -> log.info("# onNext: {}", data)); // 최종 결과 데이터를 출력

        Thread.sleep(100L); // 메인 스레드를 100밀리초 동안 대기
    }
}

//public class Example11_1 {
//    public static void main(String[] args) throws InterruptedException {
//        Mono
//            .deferContextual(ctx ->
//                Mono
//                    .just("Hello" + " " + ctx.get("firstName")) // Hello firstName가 data로 전달.
//                    .doOnNext(data -> log.info("# just doOnNext : {}", data)) // boundedElastic 스레드 생성
//            )
////            .doOnNext(data -> log.info("# just doOnNext : {}", data))
//            .subscribeOn(Schedulers.boundedElastic())
//            .publishOn(Schedulers.parallel())
//            .transformDeferredContextual(
//                    (mono, ctx) -> mono.map(data -> data + " " + ctx.get("lastName"))
//            )
//            .contextWrite(context -> context.put("lastName", "Jobs"))
//            .contextWrite(context -> context.put("firstName", "Steve")) // contextWrite부터 실행
//            .subscribe(data -> log.info("# onNext: {}", data)); // parallel에서 생성된 스레드에 의해서 실행
//
//        Thread.sleep(100L);
//    }
//}
