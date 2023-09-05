package chapter14.operator_2_filter;

import chapter14.SampleData;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.util.Map;

import static chapter14.SampleData.*;

/**
 * filterWhen 예제
 */
@Slf4j
public class Example14_17 {
    public static void main(String[] args) throws InterruptedException {
        // CovidVaccine를 키로 가지고 Tuple2<CovidVaccine, Integer> 값을 가지는 Map을 생성합니다.
        Map<CovidVaccine, Tuple2<CovidVaccine, Integer>> vaccineMap = getCovidVaccines();

        Flux
            .fromIterable(SampleData.coronaVaccineNames)
            .filterWhen(vaccine -> Mono // 비동기로 조건을 평가합니다.
                            .just(vaccineMap.get(vaccine).getT2() >= 3_000_000) // just : publisher
                            .publishOn(Schedulers.parallel())
            )
            .subscribe(data ->
                    log.info("# onNext: {}", data) // 조건을 만족하는 요소에 대한 로그를 출력합니다.
            );

        Thread.sleep(1000); // 현재 스레드를 1초 동안 대기합니다. (비동기 작업이 완료될 때까지 기다립니다)
    }
}


//public class Example14_17 {
//    public static void main(String[] args) throws InterruptedException {
//        Map<CovidVaccine, Tuple2<CovidVaccine, Integer>> vaccineMap = //Tuple2 값이 2개
//                                                                getCovidVaccines();
//        Flux
//            .fromIterable(SampleData.coronaVaccineNames)
//            .filterWhen(vaccine -> Mono
//                                    .just(vaccineMap.get(vaccine).getT2() >= 3_000_000)
//                                    .publishOn(Schedulers.parallel()))
//            .subscribe(data -> log.info("# onNext: {}", data));
//
//        Thread.sleep(1000);
//    }
//}
