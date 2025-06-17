package in.chitwan.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.chitwan.chat.repository.UserTokenRepository;

@Service
public class AuthService {

    @Autowired
    private UserTokenRepository tokenRepository;

    public boolean isTokenValid(String token) {
        return tokenRepository.findByToken(token).isPresent();
    }
}