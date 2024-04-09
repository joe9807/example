package joe.example.client;

import joe.example.entity.Example;
import joe.example.entity.ExampleKey;
import joe.example.entity.ExampleState;
import joe.example.utils.ExampleHttpClient;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
@RequiredArgsConstructor
public class KafkaClient {

    @Value("${kafka.queue.name}")
    private String queueName;

    @Value("${response.wait}")
    private String responseWait;

    private final KafkaTemplate<ExampleKey, Example> kafkaTemplate;

    public Mono<Example> sendMessage(Example example) {
        return send(example).map(exampleKeyExampleSendResult -> exampleKeyExampleSendResult.getProducerRecord().value());
    }

    private ProducerRecord<ExampleKey, Example> getProducerRecord(Example example){
        return new ProducerRecord<>(queueName, null, example.getKey("Kafka"), example, Collections.singletonList(new RecordHeader("custom_header_key", "header key ".getBytes(StandardCharsets.UTF_8))));
    }

    private Mono<SendResult<ExampleKey, Example>> send(Example example){
        return Mono.fromFuture(kafkaTemplate.send(getProducerRecord(example)));
    }

    @KafkaListener(topics = "${kafka.queue.name}", groupId = "joe_group")
    public void listener(ConsumerRecord<ExampleKey, Example> record){
        System.out.println("----------"+KafkaUtils.getConsumerGroupId()+"------------");
        printRecord(record);
        sendCallback(record);
    }

    @KafkaListener(topics = "${kafka.queue.name}", groupId = "joe_group1")
    public void listener1(ConsumerRecord<ExampleKey, Example> record){
        System.out.println("----------"+KafkaUtils.getConsumerGroupId()+"------------");
        printRecord(record);
        sendCallback(record);
    }

    private void printRecord(ConsumerRecord<ExampleKey, Example> record){
        System.out.println("key: "+record.key());
        System.out.println(record.value());
        System.out.println(record.headers());
        System.out.println("topic: "+record.topic());
        System.out.println("partition: "+record.partition());
        System.out.println("offset: "+record.offset());
    }

    private void sendCallback(ConsumerRecord<ExampleKey, Example> record){
        record.value().setState(ExampleState.UPDATED);
        ExampleHttpClient.sendRequest(record.value());
    }
}
