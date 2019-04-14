package openassemblee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ArtemisMessagingService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostConstruct
    public void init() throws Exception {
//        jmsTemplate.send("someQueue", session -> {
//            TextMessage message = session.createTextMessage();
//            message.setText("coucou loulou");
//            return message;
//        });
//        jmsTemplate.convertAndSend(orderNumber, messagePostProcessor -> {
//            message.set(messagePostProcessor);
//            return messagePostProcessor;
//        });
//
//        String messageId = message.get().getJMSMessageID();
//        LOGGER.info("sending OrderNumber='{}' with MessageId='{}'",
//            orderNumber, messageId);

//        EmbeddedActiveMQ embedded = new EmbeddedActiveMQ();
//
//        embedded.start();
//
//        ServerLocator serverLocator =  ActiveMQClient.createServerLocator("vm://0");
//
//        ClientSessionFactory factory =  serverLocator.createSessionFactory();
//
//        ClientSession session = factory.createSession();
//
//        session.createQueue("example", "example", true);
//
//        ClientProducer producer = session.createProducer("example");
//
//        ClientMessage message = session.createMessage(true);
//
//        message.putStringProperty("test", "update");
//
//        producer.send(message);
//
//        session.start();
//
//        ClientConsumer consumer = session.createConsumer("example");
//
//        ClientMessage msgReceived = consumer.receive();
//
//        System.out.println("message = " + msgReceived.getStringProperty("test"));
//
//        session.close();
    }

}
