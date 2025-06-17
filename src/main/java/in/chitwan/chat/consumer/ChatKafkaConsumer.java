package in.chitwan.chat.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ChatKafkaConsumer {

    @KafkaListener(topics = "chat", groupId = "chat_group")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
        // Dump to DB or forward to WebSocket
    }
}
