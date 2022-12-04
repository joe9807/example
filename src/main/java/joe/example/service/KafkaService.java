package joe.example.service;

import joe.example.client.KafkaClient;
import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import joe.example.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class KafkaService extends GenericMQService{
    private final KafkaClient kafkaClient;

    @Value("${kafka.callback.url}")
    private String callbackUrl;

    @Inject
    public KafkaService(KafkaClient kafkaClient, ExampleRepository repository) {
        this.kafkaClient = kafkaClient;
        this.repository = repository;
    }

    public String sendMessage() {
        Example example = Example.builder().value((int) (Math.random() * 100)).state(ExampleState.CREATED).callbackUrl(callbackUrl).build();
        return kafkaClient.sendMessage(saveExample(example));
    }

    public String sendMessages(int number) {
        List<Example> examples = IntStream.range(0, number).mapToObj(unused->Example.builder().value((int) (Math.random() * 100)).state(ExampleState.CREATED).callbackUrl(callbackUrl).build())
                .map(this::saveExample).collect(Collectors.toList());
        return kafkaClient.sendMessages(examples);
    }
}
