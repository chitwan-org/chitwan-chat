package in.chitwan.chat.controller;

import in.chitwan.chat.model.Message;
import in.chitwan.chat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload Message message) {
        // Start debugging
        logger.info("===== START MESSAGE PROCESSING =====");

        // 1. Verify method invocation
        logger.info("DEBUG: ChatController.sendMessage method invoked");

        // 2. Check if message is null
        if (message == null) {
            logger.error("CRITICAL ERROR: Received message payload is NULL!");
            return;
        }

        // 3. Log message details with proper null checks
        logger.info("DEBUG: Incoming Message details:");
        logger.info("  Sender ID: {}", message.getSenderId() != null ? message.getSenderId() : "NULL");
        logger.info("  Receiver ID: {}", message.getReceiverId() != null ? message.getReceiverId() : "NULL");
        logger.info("  Content: {}", message.getContent() != null ? message.getContent() : "NULL");
        logger.info("  Timestamp: {}", message.getSentAt() != null ? message.getSentAt() : "NULL");

        try {
            // 4. Verify ChatService injection
            if (chatService == null) {
                logger.error("CRITICAL ERROR: ChatService is NULL - Check dependency injection");
                return;
            }

            logger.info("DEBUG: Calling chatService.processMessage");

            // 5. Process the message
            chatService.processMessage(message);

            logger.info("DEBUG: chatService.processMessage completed successfully");
        } catch (Exception e) {
            // 6. Catch and log any exceptions
            logger.error("EXCEPTION during message processing: ", e);
        }

        logger.info("===== END MESSAGE PROCESSING =====");
    }

    @PostMapping("/send")
    public void sendMessageHttp(@RequestBody Message message) {
        logger.info("Received HTTP message: {}", message);
        chatService.processMessage(message);
    }
}