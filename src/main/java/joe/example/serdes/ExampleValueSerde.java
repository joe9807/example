package joe.example.serdes;

import io.swagger.v3.core.jackson.ExampleSerializer;
import joe.example.entity.Example;
import joe.example.entity.ExampleKey;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class ExampleValueSerde implements Serde<Example> {
    @Override
    public Serializer<Example> serializer() {
        return new ExampleValueSerializer();
    }

    @Override
    public Deserializer<Example> deserializer() {
        return new ExampleValueDeserializer();
    }
}
