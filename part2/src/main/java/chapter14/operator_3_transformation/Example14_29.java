package chapter14.operator_3_transformation;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * flatMap 예제
 */
@Slf4j
public class Example14_29 {
    public static void main(String[] args) {
        Flux
            .just("Good", "Bad")
            .flatMap(feeling -> Flux // flatMap :  두개의 publisher를 mapping
                                    .just("Morning", "Afternoon", "Evening")
                                    .map(time -> feeling + " " + time))
            .subscribe(log::info);
    }
}
