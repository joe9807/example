package joe.example.config;

import joe.example.entity.Example;
import joe.example.entity.ExampleKey;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {
    @Bean
    public KStream<ExampleKey, Example> kStream(StreamsBuilder streamsBuilder) {
        KStream<ExampleKey, Example> stream = streamsBuilder.stream("joe_topic");
        stream.split()
                .branch((exampleKey, example) -> example.getValue()>50, Branched.withConsumer(ks->ks.to("topic_more_then_50")))
                .branch((exampleKey, example) -> example.getValue()<=50, Branched.withConsumer(ks->ks.to("topic_less_then_50")))
                .defaultBranch(Branched.withConsumer(ks->ks.to("topic_50")));
        return stream;
    }
}
