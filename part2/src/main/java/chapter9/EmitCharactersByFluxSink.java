package chapter9;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EmitCharactersByFluxSink {

    public static void main(String[] args) {
        CharacterGenerator characterGenerator = new CharacterGenerator();
        List<Character> sequence1 = characterGenerator.generateCharacters().take(3).collectList().block();
        List<Character> sequence2 = characterGenerator.generateCharacters().take(2).collectList().block();

        log.info("sequence1: {}", sequence1);
        log.info("sequence2: {}", sequence2);

        // public class CharacterCreator {
        //    public Consumer<List<Character>> consumer; // consumer -> captured variable
        //
        //    public Flux<Character> createCharacterSequence() {
        //        return Flux.create(
        //                sink -> CharacterCreator.this.consumer =
        //                        items -> items.forEach(sink::next)); // accept 부분의 바디 // iteravale 이라 forEach 가능
        //    }

        CharacterCreator characterCreator = new CharacterCreator();
        Thread producerThread1 = new Thread(
                () -> characterCreator.consumer.accept(sequence1)); // run 메서드의 바디
        Thread producerThread2 = new Thread(
                () -> characterCreator.consumer.accept(sequence2));

        //Thread.java 핵심코드
        //   @Override
        //    public void run() {
        //        if (target != null) {
        //            target.run();
        //        }
        //    }

        List<Character> consolidated = new ArrayList<>();
        characterCreator.createCharacterSequence().subscribe(consolidated::add); //consolidated::add 메소드 참조

        try {
            producerThread1.start();
            producerThread2.start();
            producerThread1.join();
            producerThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("consolidated: {}", consolidated);

    }
}