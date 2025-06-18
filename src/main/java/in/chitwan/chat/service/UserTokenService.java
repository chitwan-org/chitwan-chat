package in.chitwan.chat.service;

import in.chitwan.chat.model.UserToken;
import in.chitwan.chat.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "access_token:";
    private static final long EXPIRATION_HOURS = 24;

    @Autowired
    public UserTokenService(UserTokenRepository userTokenRepository,
                            RedisTemplate<String, String> redisTemplate) {
        this.userTokenRepository = userTokenRepository;
        this.redisTemplate = redisTemplate;
    }

    public Long validateToken(String token) {
        // First check Redis cache
        String redisKey = REDIS_KEY_PREFIX + token;
        String userId = redisTemplate.opsForValue().get(redisKey);

        if (userId != null) {
            return Long.parseLong(userId);
        }

        // If not in cache, check PostgreSQL
        UserToken userToken = userTokenRepository.findByToken(token);
        if (userToken != null) {
            // Cache the token in Redis
            cacheToken(token, userToken.getUserId());
            return userToken.getUserId();
        }

        return null;
    }

    public void cacheToken(String token, Long userId) {
        String redisKey = REDIS_KEY_PREFIX + token;
        redisTemplate.opsForValue().set(
                redisKey,
                userId.toString(),
                EXPIRATION_HOURS,
                TimeUnit.HOURS
        );
    }
}