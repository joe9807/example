package joe.example.service.impl;

import joe.example.client.KafkaClient;
import joe.example.entity.Example;
import joe.example.repository.ExampleRepository;
import joe.example.service.MQService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements MQService {
    private final KafkaClient kafkaClient;
    private final ExampleRepository repository;

    @Value("${kafka.callback.url}")
    private String callbackUrl;

    public Example sendMessage() {
        Example example = createExample(callbackUrl);
        kafkaClient.sendMessage(repository.save(example));
        return example;
    }

    @Override
    public String receiveMessage() {
        return null;
    }

    public List<Example> sendMessages(int number) {
        List<Example> examples = IntStream.range(0, number).mapToObj(unused->createExample(callbackUrl)).map(repository::save).collect(Collectors.toList());
        kafkaClient.sendMessages(examples);
        return examples;
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
