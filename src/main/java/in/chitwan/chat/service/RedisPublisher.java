package in.chitwan.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.chitwan.chat.controller.ChatController;
import in.chitwan.chat.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    @Autowired
    private StringRedisTemplate redisTemplate;  // Using auto-configured bean

    @Autowired
    private ObjectMapper objectMapper;

    public void publish(Message message) throws JsonProcessingException {
        logger.info("reched publish");
        // Publish to user-specific channel
        String channel = "user:" + message.getReceiverId();
        String jsonMessage = objectMapper.writeValueAsString(message);
        redisTemplate.convertAndSend(channel, jsonMessage);
    }
}