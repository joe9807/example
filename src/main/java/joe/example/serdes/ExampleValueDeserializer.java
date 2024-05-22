package joe.example.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import joe.example.entity.Example;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ExampleValueDeserializer implements Deserializer<Example> {
    @Override
    public Example deserialize(String s, byte[] bytes) {
        try {
            return new ObjectMapper().readValue(bytes, Example.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
