package com.itvillage;

import java.util.Arrays;
import java.util.List;

public class Example1_2 {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 3, 21, 10, 8, 11);
        int sum = numbers.stream()
//                .parallel() // 이걸 쓰면 파티셔닝이 이루어짐. 그다음에 파이프라인 생성.
                .filter(number -> number > 6 && (number % 2 != 0)) // 함수, 파라미터가 함수형 인터페이스
                .mapToInt(number -> number) // Type AutoCasting // 각 요소를 int 형식으로 변환
                .sum();

        System.out.println("합계: " + sum);
    }
}
