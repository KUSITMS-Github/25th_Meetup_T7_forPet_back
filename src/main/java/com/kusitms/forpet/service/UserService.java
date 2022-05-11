package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import com.kusitms.forpet.repository.UserRefreshTokenRepository;
import com.kusitms.forpet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Transactional(readOnly = true)
    public UserRefreshToken findByUserIdAndRefreshToken(User user, String refreshToken) {
        return userRefreshTokenRepository.findByUserIdAndRefreshToken(user, refreshToken);
    }

    public void deleteRefreshTokenByUserId(Long userId) {
        userRefreshTokenRepository.deleteById(userId);
    }
}
