package joe.example.service.impl;

import joe.example.client.KafkaClient;
import joe.example.entity.Example;
import joe.example.repository.ExampleRepository;
import joe.example.service.MQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class KafkaServiceImpl implements MQService {
    @Autowired(required = false)
    private KafkaClient kafkaClient;

    @Autowired
    private ExampleRepository repository;

    @Value("${kafka.callback.url}")
    private String callbackUrl;

    public Mono<Example> sendMessage() {
        return repository.save(createExample(callbackUrl)).doOnNext(kafkaClient::sendMessage);
    }

    @Override
    public String receiveMessage() {
        return null;
    }

    public Flux<Example> sendMessages(int number) {
        return Flux.concat(IntStream.range(0, number).mapToObj(unused->createExample(callbackUrl)).map(repository::save).collect(Collectors.toList()))
                .doOnNext(kafkaClient::sendMessage);
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
}
