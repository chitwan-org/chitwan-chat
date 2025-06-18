package in.chitwan.chat.service;

import in.chitwan.chat.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(RedisSubscriber.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // Changed method signature to match Redis message format
    public void handleMessage(String jsonMessage, String channel) {
        try {
            Message chatMessage = objectMapper.readValue(jsonMessage, Message.class);

            String targetWebSocketTopic = "/topic/user:" + chatMessage.getReceiverId();

            messagingTemplate.convertAndSend(targetWebSocketTopic, chatMessage);

            logger.debug("Delivered message to /topic/{}: {}", channel, chatMessage);
        } catch (Exception e) {
            logger.error("Failed to process Redis message", e);
        }
    }
}