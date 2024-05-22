package joe.example.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import joe.example.entity.Example;
import joe.example.entity.ExampleKey;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public class ExampleValueSerializer implements Serializer<Example> {
    @Override
    public byte[] serialize(String topic, Example example) {
        try {
            return new ObjectMapper().writeValueAsBytes(example);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
