package joe.example.service;

import joe.example.entity.Example;
import joe.example.entity.ExampleState;

import java.util.List;

public interface MQService {
    Example sendMessage();

    String receiveMessage();

    List<Example> sendMessages(int number);

    List<Example> findAll();

    Example saveExample(Example example);

    void deleteAll();

    default Example createExample(String callbackUrl){
        return Example.builder().value((int) (Math.random() * 100)).state(ExampleState.CREATED).callbackUrl(callbackUrl).build();
    }
}
