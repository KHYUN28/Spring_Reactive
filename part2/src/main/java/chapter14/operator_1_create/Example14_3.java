package chapter14.operator_1_create;

import chapter14.SampleData;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * fromStream 예제
 */
@Slf4j
public class Example14_3 {
    public static void main(String[] args) {
        Flux
            .fromStream(() -> SampleData.coinNames.stream())
            .filter(coin -> coin.equals("BTC") || coin.equals("ETH")) // BTC와 ETH 값 필터링해서 가져옴.
            .subscribe(data -> log.info("{}", data));
    }
}
