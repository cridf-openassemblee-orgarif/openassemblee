package openassemblee.service;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;

import javax.annotation.PostConstruct;

//@Service
public class ArtemisMessagingService {

//    @Autowired
//    private JmsTemplate jmsTemplate;

    @PostConstruct
    public void init() throws Exception {
        Configuration config = new ConfigurationImpl();

//        config.addAcceptorConfiguration("in-vm", "vm://0");
        TransportConfiguration tc = new TransportConfiguration();
        tc.getParams().put("tcp", "tcp://127.0.0.1:61616");
        config.addAcceptorConfiguration(tc);

        EmbeddedActiveMQ server = new EmbeddedActiveMQ();
        server.setConfiguration(config);

        server.start();

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
