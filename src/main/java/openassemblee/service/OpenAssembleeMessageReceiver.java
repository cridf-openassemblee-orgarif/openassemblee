package openassemblee.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class OpenAssembleeMessageReceiver {

    @JmsListener(destination = "someQueue")
    public void processMessage(String content) {
        // ...
        System.out.println(content);
    }
}
