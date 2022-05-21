package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import com.kusitms.forpet.repository.UserRefreshTokenRep;
import com.kusitms.forpet.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRep userRepository;
    private final UserRefreshTokenRep userRefreshTokenRepository;

    public void save(User user) {
        userRepository.saveAndFlush(user);
    }
    public void save(UserRefreshToken token) { userRefreshTokenRepository.saveAndFlush(token); }

    @Transactional(readOnly = true)
    public User findByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Transactional(readOnly = true)
    public Optional<UserRefreshToken> findByUserIdAndRefreshToken(User user, String refreshToken) {
        return userRefreshTokenRepository.findByUserIdAndRefreshToken(user, refreshToken);
    }

    public void deleteRefreshTokenByUserId(Long userId) {
        userRefreshTokenRepository.deleteById(userId);
    }

    /**
     * 주소 등록 여부
     */
    public boolean getIsAddress(User user) {
        if(user.getAddress() != null) {
            return true;
        }
        return false;
    }


}
