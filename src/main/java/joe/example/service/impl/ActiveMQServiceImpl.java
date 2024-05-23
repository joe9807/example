package joe.example.service.impl;

import joe.example.client.ActiveMQClient;
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
public class ActiveMQServiceImpl implements MQService {
    private final ActiveMQClient activeMQClient;
    private final ExampleRepository repository;

    @Value("${activemq.callback.url}")
    private String callbackUrl;

    public Mono<Example> sendMessage() {
        return repository.save(createExample(callbackUrl)).doOnNext(activeMQClient::sendMessage);
    }

    public String receiveMessage() {
        return activeMQClient.receiveMessage();
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
    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

    @Override
    public Flux<Example> sendMessages(int number) {
        return null;
    }
}
