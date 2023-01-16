package joe.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitMQClient {
    @Value("${rabbitmq.message.broker.host}")
    private String messageBrokerHost;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.queue.name_dlx}")
    private String queueNameDlx;

    @Value("${response.wait}")
    private String responseWait;

    public String sendMessage(Example example) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(messageBrokerHost);
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            queueDeclare(channel);

            String message = new ObjectMapper().writeValueAsString(example);
            channel.basicPublish(queueName+"_exchange", "", null, message.getBytes(StandardCharsets.UTF_8));
            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String receiveMessage(boolean dlq){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(messageBrokerHost);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            queueDeclare(channel);

            String qn = dlq?queueNameDlx:queueName;
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                Example example = new ObjectMapper().readValue(message, Example.class);

                System.out.println("Message has been received: " + example + " from " + qn);
                if (!dlq) {
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
                }
                //if (true) throw new RuntimeException("11");
                example.setState(ExampleState.UPDATED);
                ExampleHttpClient.sendRequest(example);
            };
            //if true then only one consumer will fail. otherwise all consumers will fail
            //so if consumer fails before ack message then rabbitmq returns message to the queue
            //and that message will receive another consumer
            channel.basicConsume(qn, false, deliverCallback, consumerTag -> { });

            return "Consumer has started";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void queueDeclare(Channel channel) throws Exception{
        channel.exchangeDeclare(queueName+"_exchange", "direct");
        channel.exchangeDeclare("__dlx__", "direct");

        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "__dlx__");
        //args.put("x-message-ttl", 1000);
        channel.queueDeclare(queueName, false, false, false, args);
        channel.queueBind(queueName, queueName+"_exchange", "");

        channel.queueDeclare(queueNameDlx, false, false, false, new HashMap<>());
        channel.queueBind(queueNameDlx, "__dlx__", "");
    }
}
