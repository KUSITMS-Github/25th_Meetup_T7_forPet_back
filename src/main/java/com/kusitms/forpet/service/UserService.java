package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import com.kusitms.forpet.dto.response.ErrorCode;
import com.kusitms.forpet.exception.CustomException;
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

    @Transactional(rollbackFor=Exception.class)
    public void save(User user) {
        userRepository.saveAndFlush(user);
    }

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

    @Transactional(rollbackFor=Exception.class)
    public void save(UserRefreshToken token) { userRefreshTokenRepository.saveAndFlush(token); }

    @Transactional(readOnly = true)
    public UserRefreshToken findByUserIdAndRefreshToken(User user, String refreshToken) {
        Optional<UserRefreshToken> userRefreshTokenOptional = userRefreshTokenRepository.findByUserIdAndRefreshToken(user, refreshToken);

        if(userRefreshTokenOptional.isPresent()) {
            return userRefreshTokenOptional.get();
        } else {
            throw new CustomException(ErrorCode.MISMATCH_REFRESH_TOKEN);
        }
    }


    @Transactional(readOnly = true)
    public Optional<UserRefreshToken> findRefreshTokenByUserId(User user) {
        Optional<UserRefreshToken> userRefreshTokenOptional = userRefreshTokenRepository.findByUserId(user);

        return userRefreshTokenOptional;
    }

    @Transactional(rollbackFor=Exception.class)
    public void deleteRefreshTokenByUserId(Long userId) {
        userRefreshTokenRepository.deleteById(userId);
    }

}
