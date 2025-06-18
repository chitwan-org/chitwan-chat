package in.chitwan.chat.consumer;

import in.chitwan.chat.model.Message;
import in.chitwan.chat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaChatConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaChatConsumer.class);

    @Autowired
    private ChatService chatService;

    @KafkaListener(topics = "chat-messages", groupId = "chat-group")
    public void consume(Message message) {
        logger.info("Consumed message from Kafka: {}", message);
        chatService.saveMessage(message);
    }
}