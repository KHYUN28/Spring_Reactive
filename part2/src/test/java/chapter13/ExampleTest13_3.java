package chapter13;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

/**
 * StepVerifier 활용 예제
 */
public class ExampleTest13_3 {
    @Test
    public void sayHelloTest() {
        StepVerifier
                .create(GeneralTestExample.sayHello())
                .expectSubscription()
                .as("# expect subscription") // 콘솔에 출력
                .expectNext("Hi") //Hi가 없어서 오류가 남
                .as("# expect Hi")
                .expectNext("Reactor")
                .as("# expect Reactor")
                .verifyComplete();
    }
}
