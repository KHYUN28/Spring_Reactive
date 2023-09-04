package chapter11;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

/**
 * Context API 사용 예제
 */
@Slf4j
public class Example11_3 {
    public static void main(String[] args) throws InterruptedException {
        final String key1 = "company";   // 키1: 회사 이름
        final String key2 = "firstName"; // 키2: 이름
        final String key3 = "lastName";  // 키3: 성

        Mono
                .deferContextual(ctx -> // Mono.deferContextual은 콘텍스트를 사용하는 Mono를 생성합니다.
                        Mono.just(ctx.get(key1) + ", " + ctx.get(key2) + " " + ctx.get(key3)) // 콘텍스트에서 값 추출하여 문자열 생성
                )
                .publishOn(Schedulers.parallel()) // 병렬 스케줄러를 사용하여 스레드 풀에서 실행
                .contextWrite(context ->
                        context.putAll(Context.of(key2, "Steve", key3, "Jobs").readOnly()) // 새로운 콘텍스트를 만들어 key2와 key3 값을 설정
                )
                .contextWrite(context -> context.put(key1, "Apple")) // 기존 콘텍스트에 key1 값을 추가
                .subscribe(data -> log.info("# onNext: {}" , data)); // 결과 데이터를 출력

        Thread.sleep(100L); // 메인 스레드를 잠시 대기시킴
    }
}

//public class Example11_3 {
//    public static void main(String[] args) throws InterruptedException {
//        final String key1 = "company";
//        final String key2 = "firstName";
//        final String key3 = "lastName";
//
//        Mono
//            .deferContextual(ctx ->
//                    Mono.just(ctx.get(key1) + ", " + ctx.get(key2) + " " + ctx.get(key3))
//            )
//            .publishOn(Schedulers.parallel())
//            .contextWrite(context ->
//                    context.putAll(Context.of(key2, "Steve", key3, "Jobs").readOnly())
//            )
//            .contextWrite(context -> context.put(key1, "Apple"))
//            .subscribe(data -> log.info("# onNext: {}" , data));
//
//        Thread.sleep(100L);
//    }
//}
