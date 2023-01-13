package joe.example.service.impl;

import joe.example.client.ActiveMQClient;
import joe.example.entity.Example;
import joe.example.repository.ExampleRepository;
import joe.example.service.MQService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActiveMQServiceImpl implements MQService {
    private final ActiveMQClient activeMQClient;
    private final ExampleRepository repository;

    @Value("${rabbitmq.callback.url}")
    private String callbackUrl;

    public Example sendMessage() {
        Example example = createExample(callbackUrl);
        activeMQClient.sendMessage(repository.save(example));
        return example;
    }

    public String receiveMessage() {
        return activeMQClient.receiveMessage();
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

    @Override
    public List<Example> sendMessages(int number) {
        return null;
    }
}
