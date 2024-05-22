package joe.example.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import joe.example.entity.ExampleKey;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ExampleKeyDeserializer implements Deserializer<ExampleKey> {
    @Override
    public ExampleKey deserialize(String s, byte[] bytes) {
        try {
            return new ObjectMapper().readValue(bytes, ExampleKey.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
