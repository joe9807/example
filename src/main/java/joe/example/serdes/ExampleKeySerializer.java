package joe.example.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import joe.example.entity.ExampleKey;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public class ExampleKeySerializer implements Serializer<ExampleKey> {
    @Override
    public byte[] serialize(String topic, ExampleKey exampleKey) {
        try {
            return new ObjectMapper().writeValueAsBytes(exampleKey);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
