package chapter6;

import reactor.core.publisher.Flux;

/**
 * 여러개의 Flux를 연결해서 하나의 Flux로 결합하는 예제
 */
public class Example6_7 {
    public static void main(String[] args) {
        Flux.concat(
                        Flux.just("Mercury", "Venus", "Earth"),
                        Flux.just("Mars", "Jupiter", "Saturn"),
                        Flux.just("Uranus", "Neptune", "Pluto"))
                .collectList()// 리액티브 프로그래밍에서 사용되는 함수 중 하나로, 옵저버블(Observable) 스트림의 요소들을 모아서 리스트 형태로 수집하는 작업을 수행하는 메서드입니다. 이 함수는 주로 리액티브 스트림의 요소들을 컬렉션으로 수집하거나 처리할 때 사용됩니다.
                .subscribe(planets -> System.out.println(planets));
    }
}
