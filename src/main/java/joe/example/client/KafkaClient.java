package joe.example.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import joe.example.utils.ExampleHttpClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EnableKafka
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
        return examples.stream().map(example-> {
            try {
                String message = new ObjectMapper().writeValueAsString(example);
                send(message, example.getValue());
                return message;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.joining("\n"));
    }

    public String sendMessage(Example example) {
        try {
            String message = new ObjectMapper().writeValueAsString(example);
            send(message, example.getValue());
            return message;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void send(String message, int value){
        new KafkaTemplate<>(producerFactory()).send(queueName, String.valueOf(value/10),  message).addCallback(new ListenableFutureCallback<SendResult<String, String>>(){

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("message "+message+" successfully sent...");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("exception during message sending "+ ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = "${kafka.queue.name}", groupId = "joe_group")
    public void listener(String message){
        try {
            Example example = new ObjectMapper().readValue(message, Example.class);
            System.out.println(example);
            example.setState(ExampleState.UPDATED);
            ExampleHttpClient.sendRequest(example);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic(queueName, 1, (short) 1);
    }

    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, messageBrokerHost);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, messageBrokerHost);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "joe_group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
