package openassemblee.service;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.core.server.impl.ActiveMQServerImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class ArtemisMessagingService {

//    @Autowired
//    private JmsTemplate jmsTemplate;

    @PostConstruct
    public void init() throws Exception {

        Configuration configuration = new ConfigurationImpl();

        configuration.setPersistenceEnabled(true);
        configuration.setSecurityEnabled(false);

        Map<String, Object> connectionParams = new HashMap<String, Object>();
        connectionParams.put(
            org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants.PORT_PROP_NAME, 61616);

        File storeFolder = new File("/Users/mlo/temp/artemis-test-remove");

        configuration.setBindingsDirectory(storeFolder.getAbsolutePath());
        configuration.setJournalDirectory(storeFolder.getAbsolutePath());
        configuration.setLargeMessagesDirectory(storeFolder.getAbsolutePath());
        configuration.setPagingDirectory(storeFolder.getAbsolutePath());

        configuration.addAcceptorConfiguration(
            new TransportConfiguration(NettyAcceptorFactory.class.getName(), connectionParams));
        configuration.addConnectorConfiguration("connector",
            new TransportConfiguration(NettyConnectorFactory.class.getName(), connectionParams));


        ActiveMQServerImpl server = new ActiveMQServerImpl(configuration);
        server.start();

//        Configuration config = new ConfigurationImpl();
//
////        config.addAcceptorConfiguration("in-vm", "vm://0");
//        TransportConfiguration tc = new TransportConfiguration();
//        tc.getParams().put("tcp", "tcp://127.0.0.1:61616");
//        config.addAcceptorConfiguration(tc);
//
////        EmbeddedActiveMQ server = new EmbeddedActiveMQ();
////        server.setConfiguration(config);
//
////        server.start();
//
//        ActiveMQServer server = new ActiveMQServerImpl(config);
//        server.start();

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
