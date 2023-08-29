package chapter6;


import reactor.core.publisher.Mono;

/**
 * Mono 기본 개념 예제
 *  - 원본 데이터의 emit 없이 onComplete signal 만 emit 한다.
 */
public class Example6_2 {
    public static void main(String[] args) {
        Mono
            .empty()
            .subscribe(
                    none -> System.out.println("# emitted onNext signal"), // 데이터가 없으면 출력하지 않음(none)
                    error -> {},
                    () -> System.out.println("# emitted onComplete signal") // 구현 안하면 호출 안함.
            );
    }
}
