package joe.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
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
        return dlq?dlqMessage():regularMessage();
    }

    private String regularMessage(){
        try {
            Channel channel = getChannel();
            queueDeclare(channel);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Example example = printMessage(delivery, queueName);
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            };
            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });

            return "Consumer for regular queue has started";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String dlqMessage(){
        try {
            Channel channel = getChannel();
            queueDeclare(channel);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Example example = printMessage(delivery, queueNameDlx);
                example.setState(ExampleState.UPDATED);
                ExampleHttpClient.sendRequest(example);
            };
            channel.basicConsume(queueNameDlx, false, deliverCallback, consumerTag -> { });

            return "Consumer for "+ queueNameDlx+ " queue has started";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Example printMessage(Delivery delivery, String queueName) {
        Example example = null;
        try {
            example = new ObjectMapper().readValue(new String(delivery.getBody(), StandardCharsets.UTF_8), Example.class);

            System.out.println(delivery.getEnvelope().getRoutingKey() + "; " + delivery.getEnvelope().getExchange() + "; Message has been received: " + example + " from " + queueName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return example;
    }

    private Channel getChannel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(messageBrokerHost);
        Connection connection = factory.newConnection();
        return connection.createChannel();
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
