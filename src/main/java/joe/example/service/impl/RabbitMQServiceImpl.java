package joe.example.service.impl;

import joe.example.client.RabbitMQClient;
import joe.example.entity.Example;
import joe.example.repository.ExampleRepository;
import joe.example.service.MQService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RabbitMQServiceImpl implements MQService {
    private final RabbitMQClient rabbitMQClient;
    private final ExampleRepository repository;

    @Value("${rabbitmq.callback.url}")
    private String callbackUrl;

    public Mono<Example> sendMessage(){
        //return repository.save(createExample(callbackUrl)).doOnNext(example-> repository.findById(example.getId())).doOnNext(rabbitMQClient::sendMessage);
        return repository.save(createExample(callbackUrl)).doOnNext(rabbitMQClient::sendMessage);
    }

    public String receiveMessage(boolean dlq){
        return rabbitMQClient.receiveMessage(dlq);
    }

    @Override
    public String receiveMessage() {
        return null;
    }

    @Override
    public Flux<Example> sendMessages(int number) {
        return null;
    }

    @Override
    public Flux<Example> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Example> saveExample(Example example) {
        return repository.save(example);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
