package in.chitwan.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.chitwan.chat.producer.UserToken;
import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByToken(String token);
}
