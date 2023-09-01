package chapter9;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class FluxPushPullExample {

    // 메시지 리스너 인터페이스 정의
    interface MyMessageListener<T> {
        void onMessage(List<T> messages); // 메시지 처리 이벤트를 위한 인터페이스 메서드
    }

    // 가상의 메시지 프로세서
    static class MyMessageProcessor {
        private MyMessageListener<String> listener; // 메시지 리스너 저장

        public void register(MyMessageListener<String> listener) {
            this.listener = listener; // 메시지 리스너 등록
        }

        // 메시지의 시뮬레이션 메서드
        public List<String> getHistory(long n) {
            return IntStream.range(0, (int) n)
                    .mapToObj(i -> "History-" + i) // 가상의 메시지를 생성하여 리스트로 반환
                    .collect(Collectors.toList()); // 종단 연산자 // <R, A> R collect(Collector<? super T, A, R> collector);
                    // toList : Collector<T, ?, List<T>> 
        }
        
        //  Collector<T, ?, List<T>> toList() {
        //  return new CollectorImpl<>((Supplier<List<T>>) ArrayList::new, List::add,
        //            (left, right) -> { left.addAll(right); return left; },
        //             CH_ID);
        //  } 추상클래스나 인터페이스를 구현하는게 익명클래스

        // 실제 메시지 발송 메서드 (예시)
        public void dispatchMessages() {
            listener.onMessage(List.of("msg1", "msg2", "msg3")); // 등록된 리스너에게 메시지 전달
        }
    }

    public static void main(String[] args) {

        MyMessageProcessor myMessageProcessor = new MyMessageProcessor(); // 메시지 프로세서 객체 생성

        Flux<String> bridge = Flux.create(sink -> {
            // register MyMessageListener
            myMessageProcessor.register(messages -> {
                log.info("called onMessage"); // 메시지 도착 시 호출되는 이벤트

                for (String s : messages) {
                    sink.next(s); // 도착한 메시지를 Flux 스트림으로 전송
                }
            });

            sink.onRequest(n -> {
                if (n == Long.MAX_VALUE) {
                    log.warn("Unlimited request detected. Limiting to 1000 messages.");
                    n = 10; // 무제한 요청을 감지하면 제한을 설정하여 메시지 수를 제한
                }

                List<String> messages = myMessageProcessor.getHistory(n); // 요청 시에 과거 메시지를 가져옴
                for (String s : messages) {
                    log.info("onRequest: message = {}", s);
                    sink.next(s); // 요청에 따라 즉시 메시지를 스트림으로 전송
                }
            });
        });

        bridge.subscribe(
                data -> System.out.println("Received: " + data), // 메시지 수신 시 처리
                err -> System.err.println("Error: " + err.getMessage()), // 에러 처리
                () -> System.out.println("Stream completed") // 스트림 완료 시 처리
        );

        // 메시지 발송 시뮬레이션
//        myMessageProcessor.dispatchMessages();
    }
}

//@Slf4j
//public class FluxPushPullExample {
//
//    // 메시지 리스너 인터페이스 정의
//    interface MyMessageListener<T> {
//        void onMessage(List<T> messages);
//    }
//
//    // 가상의 메시지 프로세서
//    static class MyMessageProcessor {
//        private MyMessageListener<String> listener;
//
//        public void register(MyMessageListener<String> listener) {
//            this.listener = listener;
//        }
//
//        // 메시지의 시뮬레이션 메서드
//        public List<String> getHistory(long n) {
//            return IntStream.range(0, (int) n)
//                    .mapToObj(i -> "History-" + i)
//                    .collect(Collectors.toList());
//        }
//
//        // 실제 메시지 발송 메서드 (예시)
//        public void dispatchMessages() {
//            listener.onMessage(List.of("msg1", "msg2", "msg3"));
//        }
//    }
//
//    public static void main(String[] args) {
//        MyMessageProcessor myMessageProcessor = new MyMessageProcessor();
//
//        Flux<String> bridge = Flux.create(sink -> {
//            // register MyMessageListener
//            myMessageProcessor.register(messages -> {
//                log.info("called onMessage");
//
//                for (String s : messages) {
//                    sink.next(s); // The remaining messages that arrive asynchronously later are also delivered.
//                }
//            });
//
//            sink.onRequest(n -> {
//                if (n == Long.MAX_VALUE) {
//                    log.warn("Unlimited request detected. Limiting to 1000 messages.");
//                    n = 10; // or another reasonable limit
//                }
//
//                List<String> messages = myMessageProcessor.getHistory(n); // Poll for messages when requests are made.
//                for (String s : messages) {
//                    log.info("onRequest: message = {}", s);
//                    sink.next(s); // If messages are available immediately, push them to the sink.
//                }
//            });
//        });
//
//        bridge.subscribe(
//                data -> System.out.println("Received: " + data),
//                err -> System.err.println("Error: " + err.getMessage()),
//                () -> System.out.println("Stream completed")
//        );
//
//        // 메시지 발송 시뮬레이션
////        myMessageProcessor.dispatchMessages();
//    }
//}
