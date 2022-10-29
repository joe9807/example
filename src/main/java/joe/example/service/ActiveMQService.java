package joe.example.service;

import joe.example.activemq.ActiveMQClient;
import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import joe.example.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class ActiveMQService {
    private final ActiveMQClient activeMQClient;
    private final ExampleRepository repository;

    @Value("${rabbitmq.callback.url}")
    private String callbackUrl;

    @Inject
    public ActiveMQService(ActiveMQClient activeMQClient, ExampleRepository repository) {
        this.activeMQClient = activeMQClient;
        this.repository = repository;
    }

    public String sendMessage() {
        return activeMQClient.sendMessage(saveExample(Example.builder().value((int) (Math.random() * 100)).state(ExampleState.CREATED).callbackUrl(callbackUrl).build()));
    }

    public String receiveMessage(){
        return activeMQClient.receiveMessage();
    }

    public Example saveExample(Example example) {
        return repository.save(example);
    }

    public List<Example> findAll(){
        return repository.findAll();
    }
}
