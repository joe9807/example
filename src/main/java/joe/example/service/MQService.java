package joe.example.service;

import joe.example.entity.Example;

import java.util.List;

public interface MQService {
    String sendMessage();

    String receiveMessage();

    List<Example> findAll();

    Example saveExample(Example example);

    void deleteAll();
}
