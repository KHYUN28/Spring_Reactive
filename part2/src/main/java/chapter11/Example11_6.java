package chapter11;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Context의 특징 예제
 *  - Context는 Operator 체인의 아래에서부터 위로 전파된다.
 *      - 따라서 Operator 체인 상에서 Context read 메서드가 Context write 메서드 밑에 있을 경우에는 write된 값을 read할 수 없다.
 */
@Slf4j
public class Example11_6 {
    public static void main(String[] args) throws InterruptedException {
        String key1 = "company"; // key1 변수에 문자열 "company"를 할당합니다.
        String key2 = "name";    // key2 변수에 문자열 "name"을 할당합니다.

        Mono
                .deferContextual(ctx -> // Mono를 생성하고, 콘텍스트를 사용하는 람다 함수를 정의합니다.
                        Mono.just(ctx.get(key1)) // 콘텍스트에서 key1에 해당하는 값을 가져와서 Mono로 반환합니다.
                )
                .publishOn(Schedulers.parallel()) // 병렬 스케줄러로 스위치합니다.
                .contextWrite(context -> context.put(key2, "Bill")) // 콘텍스트에 key2를 "Bill"로 설정합니다.
                .transformDeferredContextual((mono, ctx) ->
                        mono.map(data -> data + ", " + ctx.getOrDefault(key2, "Steve")) // Mono의 데이터와 콘텍스트의 key2 값을 조합합니다.
                ) // mono.map은 새로운 모노를 만들어냄.
                .contextWrite(context -> context.put(key1, "Apple")) // 콘텍스트에 key1을 "Apple"로 설정합니다.
                .subscribe(data -> log.info("# onNext: {}", data)); // 구독하고 onNext 이벤트를 로깅합니다.

        Thread.sleep(100L); // 100 밀리초 동안 메인 스레드를 대기합니다.
    }
}

//public class Example11_6 {
//    public static void main(String[] args) throws InterruptedException {
//        String key1 = "company";
//        String key2 = "name";
//
//        Mono
//            .deferContextual(ctx ->
//                Mono.just(ctx.get(key1))
//            )
//            .publishOn(Schedulers.parallel())
//            .contextWrite(context -> context.put(key2, "Bill"))
//            .transformDeferredContextual((mono, ctx) ->
//                    mono.map(data -> data + ", " + ctx.getOrDefault(key2, "Steve"))
//            )
//            .contextWrite(context -> context.put(key1, "Apple"))
//            .subscribe(data -> log.info("# onNext: {}", data));
//
//        Thread.sleep(100L);
//    }
//}
