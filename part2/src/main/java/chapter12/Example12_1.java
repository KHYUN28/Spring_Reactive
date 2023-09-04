package chapter12;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

/**
 * onOperatorDebug() Hook 메서드를 이용한 Debug mode 예
 * - 애플리케이션 전체에서 global 하게 동작한다.
 */
@Slf4j
public class Example12_1 {
    public static Map<String, String> fruits = new HashMap<>(); // 과일 이름과 번역을 저장하는 맵을 생성합니다.

    static {
        fruits.put("banana", "바나나"); // 맵에 "banana"와 "바나나"를 추가합니다.
        fruits.put("apple", "사과");   // 맵에 "apple"과 "사과"를 추가합니다.
        fruits.put("pear", "배");      // 맵에 "pear"와 "배"를 추가합니다.
        fruits.put("grape", "포도");   // 맵에 "grape"와 "포도"를 추가합니다.
    }

    public static void main(String[] args) throws InterruptedException {
        Hooks.onOperatorDebug(); // 리액터(Reactor) 연산자 디버그 모드를 활성화합니다.

        Flux
//                .fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "MELONS"})
                .fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "GRAPES"}) // 문자열 배열을 Flux로 변환합니다.
                .subscribeOn(Schedulers.boundedElastic()) // 구독(subscription)을 boundedElastic 스케줄러에서 처리하도록 설정합니다.
                .publishOn(Schedulers.parallel()) // 연산을 parallel 스케줄러에서 병렬로 처리하도록 설정합니다.
                .map(String::toLowerCase) // 문자열을 소문자로 변환합니다.
                .map(fruit -> fruit.substring(0, fruit.length() - 1)) // 문자열의 마지막 문자를 제거합니다.
                .map(fruits::get) // 과일 이름을 번역된 문자열로 매핑합니다.
                .map(translated -> "맛있는 " + translated) // 번역된 문자열에 "맛있는 "을 추가합니다.
                .subscribe(
                        log::info, // 성공적으로 처리된 결과를 로그로 출력합니다.
                        error -> log.error("# onError:", error)); // 에러 발생 시 에러 메시지를 로그로 출력합니다.

        Thread.sleep(100L); // 잠시 대기하여 비동기 작업이 완료될 때까지 기다립니다.
    }
}

//public class Example12_1 {
//    public static Map<String, String> fruits = new HashMap<>();
//
//    static {
//        fruits.put("banana", "바나나");
//        fruits.put("apple", "사과");
//        fruits.put("pear", "배");
//        fruits.put("grape", "포도");
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        Hooks.onOperatorDebug(); // 애를 쓰면 디버그
//
//        Flux
////                .fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "MELONS"})
//                .fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "GRAPES"})
//                .subscribeOn(Schedulers.boundedElastic())
//                .publishOn(Schedulers.parallel())
//                .map(String::toLowerCase)
//                .map(fruit -> fruit.substring(0, fruit.length() - 1))
//                .map(fruits::get)
//                .map(translated -> "맛있는 " + translated)
//                .subscribe(
//                        log::info,
//                        error -> log.error("# onError:", error));
//
//        Thread.sleep(100L);
//    }
//}
