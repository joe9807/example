package joe.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import joe.example.utils.ExampleHttpClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class KafkaClient {

    @Value("${kafka.queue.name}")
    private String queueName;

    @Value("${response.wait}")
    private String responseWait;

    @Autowired
    private KafkaTemplate<String, Example> kafkaTemplate;

    public void sendMessages(List<Example> examples){
        examples.forEach(this::sendMessage);
    }

    public void sendMessage(Example example) {
        try {
            send(String.valueOf(example.getValue()/10), example);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void send(String key, Example value){
        ProducerRecord<String, Example> record = new ProducerRecord<>(queueName, null, key, value
                , Collections.singletonList(new RecordHeader("custom_header_key", "header key ".getBytes(StandardCharsets.UTF_8))));
        kafkaTemplate.send(record).addCallback(new ListenableFutureCallback<SendResult<String, Example>>(){
            @Override
            public void onSuccess(SendResult<String, Example> result) {
                System.out.println("message "+result.getProducerRecord().value()+" successfully sent...");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("exception during message sending "+ ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = "${kafka.queue.name}", groupId = "joe_group")
    public void listener(ConsumerRecord<String, String> record, Example example){
        try {
            System.out.println("----------------------");
            System.out.println("key: "+record.key());
            System.out.println(example);
            System.out.println(record.headers());
            System.out.println("topic: "+record.topic());
            System.out.println("partition: "+record.partition());
            System.out.println("offset: "+record.offset());
            example.setState(ExampleState.UPDATED);
            ExampleHttpClient.sendRequest(example);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
