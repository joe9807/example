package joe.example.service.impl;

import joe.example.client.RabbitMQClient;
import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import joe.example.repository.ExampleRepository;
import joe.example.service.MQService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RabbitMQServiceImpl implements MQService {
    private final RabbitMQClient rabbitMQClient;
    private final ExampleRepository repository;

    @Value("${rabbitmq.callback.url}")
    private String callbackUrl;

    public String sendMessage(){
        Example example = Example.builder().value((int) (Math.random() * 100)).state(ExampleState.CREATED).callbackUrl(callbackUrl).build();
        return rabbitMQClient.sendMessage(repository.save(example));
    }

    public String receiveMessage(){
        return rabbitMQClient.receiveMessage();
    }

    @Override
    public String sendMessages(int number) {
        return null;
    }

    @Override
    public List<Example> findAll() {
        return repository.findAll();
    }

    @Override
    public Example saveExample(Example example) {
        return repository.save(example);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
