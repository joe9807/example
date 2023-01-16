package joe.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import joe.example.entity.Example;
import joe.example.entity.ExampleState;
import joe.example.utils.ExampleHttpClient;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class ActiveMQClient {
    @Value("${activemq.message.broker.url}")
    private String messageBrokerUrl;

    @Value("${activemq.queue.name}")
    private String queueName;

    @Value("${response.wait}")
    private String responseWait;

    private MessageConsumer messageConsumer;

    public String sendMessage(Example example) {
        try {
            Connection connection = new ActiveMQConnectionFactory(messageBrokerUrl).createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(session.createQueue(queueName));
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            String str = new ObjectMapper().writeValueAsString(example);
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(str);
            messageProducer.send(textMessage);

            messageProducer.close();
            session.close();
            connection.close();
            return str;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String receiveMessage() {
        if (messageConsumer != null) {
            return responseWait;
        }

        try {
            Connection connection = new ActiveMQConnectionFactory(messageBrokerUrl).createConnection();
            connection.start();
            connection.setExceptionListener(Throwable::printStackTrace);
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageConsumer = session.createConsumer(session.createQueue(queueName));
            messageConsumer.setMessageListener(message -> {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    Example example = new ObjectMapper().readValue(textMessage.getText(), Example.class);
                    example.setState(ExampleState.UPDATED);
                    ExampleHttpClient.sendRequest(example);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return responseWait;
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
