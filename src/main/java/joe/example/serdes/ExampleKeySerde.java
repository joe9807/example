package joe.example.serdes;

import joe.example.entity.ExampleKey;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class ExampleKeySerde implements Serde<ExampleKey> {
    @Override
    public Serializer<ExampleKey> serializer() {
        return new ExampleKeySerializer();
    }

    @Override
    public Deserializer<ExampleKey> deserializer() {
        return new ExampleKeyDeserializer();
    }
}
