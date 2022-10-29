package joe.example.activemq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import joe.example.utils.ExampleHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RabbitMQClient {
    @Value("${rabbitmq.message.broker.host}")
    private String messageBrokerHost;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${response.wait}")
    private String responseWait;

    public String sendMessage(Example example) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(messageBrokerHost);
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, false, false, false, null);

            String message = new ObjectMapper().writeValueAsString(example);
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String receiveMessage(){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(messageBrokerHost);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(queueName, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                System.out.println("Consumer has started.");
                if (true) throw new RuntimeException("11");
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                Example example = new ObjectMapper().readValue(message, Example.class);
                example.setState(ExampleState.UPDATED);
                ExampleHttpClient.sendRequest(example);
            };
            //if false than all only one consumer will fail. otherwise all consumers will fail
            //so if consumer fails before ack message then rabbitmq returns message to the queue
            //and that message will receive another consumer
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

            return responseWait;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
