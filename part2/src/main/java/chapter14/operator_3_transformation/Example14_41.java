package chapter14.operator_3_transformation;

import chapter14.SampleData;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * collectMap 예제
 */
@Slf4j
public class Example14_41 {
    public static void main(String[] args) {
        // Flux를 생성하고, 0부터 25까지의 숫자를 방출하는 Flux를 만듭니다.
        Flux
                .range(0, 26) // collectMap 연산자를 사용하여 Flux의 요소를 매핑하여 Map으로 수집합니다.
                .collectMap( // key 매핑 함수: SampleData.morseCodes 배열을 사용하여 숫자를 모스 부호에 매핑합니다.
                        key -> SampleData.morseCodes[key], // value 매핑 함수: 숫자를 알파벳 문자로 변환합니다.
                        value -> transformToLetter(value)) // 구독하고 onNext 이벤트를 처리하는 콜백 함수를 등록합니다.
                .subscribe(
                        map -> log.info("# onNext: {}", map)); // onNext 콜백: 수집된 Map을 출력합니다.
    }

    // 주어진 숫자를 알파벳 문자로 변환하는 메서드
    private static String transformToLetter(int value) {
        return Character.toString((char) ('a' + value));
    }
}

//public class Example14_41 {
//    public static void main(String[] args) {
//        Flux
//            .range(0, 26)
//            .collectMap(key -> SampleData.morseCodes[key],
//                    value -> transformToLetter(value))
//            .subscribe(map -> log.info("# onNext: {}", map));
//    }
//
//    private static String transformToLetter(int value) {
//        return Character.toString((char) ('a' + value));
//    }
//}