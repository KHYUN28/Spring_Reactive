package chapter14.operator_7_split;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * split 예제
 *  - bufferTimeout(maxSize, maxTime) Operator
 *      - Upstream에서 emit되는 첫 번째 데이터부터 maxSize 숫자 만큼의 데이터 또는 maxTime 내에 emit된 데이터를 List 버퍼로 한번에 emit한다.
 *      - maxSize나 maxTime에서 먼저 조건에 부합할때까지 emit된 데이터를 List 버퍼로 emit한다.
 *      - 마지막 버퍼가 포함하는 데이터는 maxSize보다 작거나 같다.
 */
@Slf4j
public class Example14_56 {
    public static void main(String[] args) {
        log.info("Curent Time = {}" , LocalDateTime.now());
        Flux
                .range(1, 20) // 1부터 20까지의 숫자를 발생시키는 Flux 생성
                .map(num -> {
                    try {
                        if (num < 10) {
                            Thread.sleep(100L); // 숫자가 10보다 작으면 100밀리초 동안 스레드를 일시 정지
                        } else {
                            Thread.sleep(300L); // 숫자가 10 이상이면 300밀리초 동안 스레드를 일시 정지
                        }
                    } catch (InterruptedException e) {}
                    return num; // 스레드 일시 정지 후에 현재의 숫자를 반환
                })
                .bufferTimeout(3, Duration.ofMillis(400L))
                // 버퍼에 요소를 모으는데 사용되며, 3개의 요소를 모을 때까지 기다리거나, 400밀리초마다 모아서 새로운 버퍼로 만듦
                .subscribe(buffer -> log.info("# onNext: {} {}", buffer,LocalDateTime.now())); // 구독하고 있는 버퍼 내용을 로그로 출력
    }
}

//public class Example14_56 {
//    public static void main(String[] args) {
//        Flux
//            .range(1, 20)
//            .map(num -> {
//                try {
//                    if (num < 10) {
//                        Thread.sleep(100L);
//                    } else {
//                        Thread.sleep(300L);
//                    }
//                } catch (InterruptedException e) {}
//                return num;
//            })
//            .bufferTimeout(3, Duration.ofMillis(400L)) // 400ms 안에 3개가 들어오면 List로 전달
//            .subscribe(buffer -> log.info("# onNext: {}", buffer));
//    }
//}