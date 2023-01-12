package joe.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import joe.example.utils.ExampleHttpClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class KafkaClient {

    @Value("${kafka.message.broker.host}")
    private String messageBrokerHost;

    @Value("${kafka.queue.name}")
    private String queueName;

    @Value("${response.wait}")
    private String responseWait;

    public String sendMessages(List<Example> examples){
        return examples.stream().map(this::sendMessage).collect(Collectors.joining("\n"));
    }

    public String sendMessage(Example example) {
        try {
            String message = new ObjectMapper().writeValueAsString(example);
            send(String.valueOf(example.getValue()/10), message);
            return message;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void send(String key, String value){
        ProducerRecord<String, String> record = new ProducerRecord<>(queueName, null, key, value
                , Collections.singletonList(new RecordHeader("custom_header_key", "header key ".getBytes(StandardCharsets.UTF_8))));
        new KafkaTemplate<>(producerFactory()).send(record).addCallback(new ListenableFutureCallback<SendResult<String, String>>(){
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("message "+value+" successfully sent...");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("exception during message sending "+ ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = "${kafka.queue.name}", groupId = "joe_group")
    public void listener(ConsumerRecord<String, String> record){
        try {
            Example example = new ObjectMapper().readValue(record.value(), Example.class);
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

    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, messageBrokerHost);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
}
