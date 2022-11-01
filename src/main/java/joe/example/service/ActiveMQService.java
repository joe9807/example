package joe.example.service;

import joe.example.client.ActiveMQClient;
import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import joe.example.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class ActiveMQService extends GenericMQService {
    private final ActiveMQClient activeMQClient;

    @Value("${rabbitmq.callback.url}")
    private String callbackUrl;

    @Inject
    public ActiveMQService(ActiveMQClient activeMQClient, ExampleRepository repository) {
        this.activeMQClient = activeMQClient;
        this.repository = repository;
    }

    public String sendMessage() {
        Example example = Example.builder().value((int) (Math.random() * 100)).state(ExampleState.CREATED).callbackUrl(callbackUrl).build();
        return activeMQClient.sendMessage(saveExample(example));
    }

    public String receiveMessage(){
        return activeMQClient.receiveMessage();
    }

}
