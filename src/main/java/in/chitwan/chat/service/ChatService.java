package in.chitwan.chat.service;

import in.chitwan.chat.model.Message;
import in.chitwan.chat.producer.KafkaChatProducer;
import in.chitwan.chat.repository.MessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private RedisPublisher redisPublisher;

    @Autowired
    private KafkaChatProducer kafkaProducer;

    @Autowired
    private MessageRepository messageRepository;

    public void processMessage(Message message) {
        try {
            logger.debug("Processing message: {}", message);

            // Validate message
            if (message.getSenderId() == null) {
                throw new IllegalArgumentException("Sender ID cannot be null");
            }
            if (message.getReceiverId() == null) {
                throw new IllegalArgumentException("Receiver ID cannot be null");
            }
            if (message.getContent() == null || message.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("Message content cannot be empty");
            }

//             Ensure timestamp is set
            if (message.getSentAt() == null) {
                message.setSentAt(LocalDateTime.now());
            }

            logger.debug("Processing message: success");

            // Send to Redis for real-time delivery
            redisPublisher.publish(message);

            // Send to Kafka for persistent storage
            kafkaProducer.sendMessage(message);

            logger.info("Message processed successfully");
        } catch (Exception e) {
            logger.error("Failed to publish to Redis", e);

        }

        kafkaProducer.sendMessage(message);
    }

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }
}