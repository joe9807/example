package joe.example.client;

import joe.example.entity.Example;
import joe.example.entity.ExampleKey;
import joe.example.entity.ExampleState;
import joe.example.utils.ExampleHttpClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
public class KafkaClient {

    @Value("${kafka.queue.name}")
    private String queueName;

    @Value("${response.wait}")
    private String responseWait;

    @Autowired
    private KafkaTemplate<ExampleKey, Example> kafkaTemplate;

    public void sendMessages(List<Example> examples){
        examples.forEach(this::sendMessage);
    }

    public void sendMessage(Example example) {
        send(example);
    }

    private ProducerRecord<ExampleKey, Example> getProducerRecord(Example example){
        return new ProducerRecord<>(queueName, null, example.getKey("Kafka"), example, Collections.singletonList(new RecordHeader("custom_header_key", "header key ".getBytes(StandardCharsets.UTF_8))));
    }

    private void send(Example example){
        kafkaTemplate.send(getProducerRecord(example)).addCallback(new ListenableFutureCallback<SendResult<ExampleKey, Example>>(){
            @Override
            public void onSuccess(SendResult<ExampleKey, Example> result) {
                System.out.println("message "+result.getProducerRecord().value()+" successfully sent...");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("exception during message sending "+ ex.getMessage());
            }
        });
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
