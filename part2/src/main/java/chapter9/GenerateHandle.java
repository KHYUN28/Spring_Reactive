package chapter9;

import reactor.core.publisher.Flux;

public class GenerateHandle {

    // 알파벳 생성 메서드
    public static String alphabet(int letterNumber) {
        // letterNumber가 유효한 범위인지 확인
        if (letterNumber < 1 || letterNumber > 26) {
            return null; // 범위를 벗어나면 null 반환
        }
        int letterIndexAscii = 'A' + letterNumber - 1; // 알파벳의 ASCII 코드 계산
        return "" + (char) letterIndexAscii; // 해당 알파벳 반환
    }

    public static void main(String[] args) {
        // Flux 생성 및 처리
        Flux<String> alphabet = Flux.just(-1, 30, 13, 9, 20)
                .handle((i, sink) -> {
                    String letter = alphabet(i); // 주어진 숫자로 알파벳 생성
                    if (letter != null)
                        sink.next(letter); // 생성된 알파벳을 Flux에 넣어줌
                });

        // Flux 구독하여 출력
        alphabet.subscribe(System.out::println);
    }
}


//public class GenerateHandle {
//
//    public static String alphabet(int letterNumber) {
//        if (letterNumber < 1 || letterNumber > 26) {
//            return null;
//        }
//        int letterIndexAscii = 'A' + letterNumber - 1;
//        return "" + (char) letterIndexAscii;
//    }
//
//    // Flux<R> handle(BiConsumer<T, SynchronousSink<R>>);
//    // BiConsumer의 functional method : void accept(T t, U u);
//    public static void main(String[] args) {
//        Flux<String> alphabet = Flux.just(-1, 30, 13, 9, 20)
//                .handle((i, sink) -> {
//                    String letter = alphabet(i);
//                    if (letter != null)
//                        sink.next(letter);
//                });
//
//        alphabet.subscribe(System.out::println);
//    }
//}
