package chapter12;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * log() operator를 사용한 예제
 */
@Slf4j
public class Example12_7 {
    public static Map<String, String> fruits = new HashMap<>();

    static {
        fruits.put("banana", "바나나");
        fruits.put("apple", "사과");
        fruits.put("pear", "배");
        fruits.put("grape", "포도");
    }

    public static void main(String[] args) {
        // 과일 이름과 번역을 저장하는 Map을 초기화합니다.

        Flux.fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "GRAPES"}) // 문자열 배열을 Flux로 변환합니다.
                .map(String::toLowerCase) // 모든 문자열을 소문자로 변환합니다.
                .map(fruit -> fruit.substring(0, fruit.length() - 1)) // 문자열의 마지막 문자를 제거합니다.
                .log() // 연산 중간 결과를 로그로 출력합니다.
//                .log("Fruit.Substring", Level.FINE) // 특정 로그 레벨로 로그를 출력할 수도 있습니다.
                .map(fruits::get) // 과일 이름을 번역된 문자열로 매핑합니다.
                .subscribe(
                        log::info, // 성공적으로 처리된 결과를 로그로 출력합니다.
                        error -> log.error("# onError:", error) // 에러 발생 시 에러 메시지를 로그로 출력합니다.
                );
    }
}

//public class Example12_7 {
//    public static Map<String, String> fruits = new HashMap<>();
//
//    static {
//        fruits.put("banana", "바나나");
//        fruits.put("apple", "사과");
//        fruits.put("pear", "배");
//        fruits.put("grape", "포도");
//    }
//
//    public static void main(String[] args) {
//        Flux.fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "MELONS"})
//                .map(String::toLowerCase)
//                .map(fruit -> fruit.substring(0, fruit.length() - 1))
//                .log()
////                .log("Fruit.Substring", Level.FINE)
//                .map(fruits::get)
//                .subscribe(
//                        log::info,
//                        error -> log.error("# onError:", error));
//    }
//}
