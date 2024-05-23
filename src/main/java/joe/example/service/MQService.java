package joe.example.service;

import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MQService {
    Mono<Example> sendMessage();

    String receiveMessage();

    Flux<Example> sendMessages(int number);

    Flux<Example> findAll();

    Mono<Example> saveExample(Example example);

    Mono<Void> deleteAll();

    default String receiveMessage(boolean dlq){
        return null;
    }

    default Example createExample(String callbackUrl){
        return Example.builder().value((long) (Math.random() * 100)).state(ExampleState.CREATED).callbackUrl(callbackUrl).build();
    }
}
