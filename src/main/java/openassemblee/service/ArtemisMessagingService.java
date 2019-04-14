package openassemblee.service;

import org.apache.activemq.artemis.api.core.client.*;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.springframework.stereotype.Service;

@Service
public class ArtemisMessagingService {

//    @PostConstruct
    public void init() throws Exception {
        EmbeddedActiveMQ embedded = new EmbeddedActiveMQ();

        embedded.start();

        ServerLocator serverLocator =  ActiveMQClient.createServerLocator("vm://0");

        ClientSessionFactory factory =  serverLocator.createSessionFactory();

        ClientSession session = factory.createSession();

        session.createQueue("example", "example", true);

        ClientProducer producer = session.createProducer("example");

        ClientMessage message = session.createMessage(true);

        message.putStringProperty("test", "update");

        producer.send(message);

        session.start();

        ClientConsumer consumer = session.createConsumer("example");

        ClientMessage msgReceived = consumer.receive();

        System.out.println("message = " + msgReceived.getStringProperty("test"));

        session.close();
    }

}
