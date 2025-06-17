package in.chitwan.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.chitwan.chat.producer.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {}