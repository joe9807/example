package joe.example.service;

import joe.example.client.RabbitMQClient;
import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import joe.example.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMQService {
    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private ExampleRepository repository;

    @Value("${rabbitmq.callback.url}")
    private String callbackUrl;

    public String send(){
        Example example = Example.builder().value((int) (Math.random() * 100)).state(ExampleState.CREATED).callbackUrl(callbackUrl).build();
        return rabbitMQClient.sendMessage(repository.save(example));
    }

    public String receiveMessage(){
        return rabbitMQClient.receiveMessage();
    }

    public List<Example> findAll(){
        return repository.findAll();
    }

    public Example saveExample(Example example) {
        return repository.save(example);
    }
}
