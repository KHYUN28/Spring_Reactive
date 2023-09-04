package chapter11;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * ContextView API 사용 예제
 */
@Slf4j
public class Example11_4 {
    public static void main(String[] args) throws InterruptedException {
        final String key1 = "company";   // 키1: 회사 이름
        final String key2 = "firstName"; // 키2: 이름
        final String key3 = "lastName";  // 키3: 성

        Mono
                .deferContextual(ctx ->
                        Mono.just(ctx.get(key1) + ", " +
                                ctx.getOrEmpty(key2).orElse("no firstName") + " " +
                                ctx.getOrDefault(key3, "no lastName"))
                )
                .publishOn(Schedulers.parallel()) // 병렬 스레드 풀에서 연산 실행
                .contextWrite(context -> context.put(key1, "Apple")) // 콘텍스트에 "company" 값을 추가
                .subscribe(data -> log.info("# onNext: {}" , data)); // 결과 데이터를 로그로 출력

        Thread.sleep(100L); // 메인 스레드를 100밀리초 동안 대기
    }
}

//public class Example11_4 {
//    public static void main(String[] args) throws InterruptedException {
//        final String key1 = "company";
//        final String key2 = "firstName";
//        final String key3 = "lastName";
//
//        Mono
//            .deferContextual(ctx ->
//                    Mono.just(ctx.get(key1) + ", " +
//                            ctx.getOrEmpty(key2).orElse("no firstName") + " " +
//                            ctx.getOrDefault(key3, "no lastName"))
//            )
//            .publishOn(Schedulers.parallel())
//            .contextWrite(context -> context.put(key1, "Apple"))
//            .subscribe(data -> log.info("# onNext: {}" , data));
//
//        Thread.sleep(100L);
//    }
//}
