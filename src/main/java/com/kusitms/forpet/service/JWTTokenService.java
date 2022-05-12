package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import com.kusitms.forpet.repository.UserRefreshTokenRepository;
import com.kusitms.forpet.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class JWTTokenService {
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final TokenProvider tokenProvider;

    /*
    회원가입 시 사용되는 JWT 발급
     */
    public List<String> createJWTToken(User user) {
        // access token 발급
        String accessToken = tokenProvider.createAccessToken(user.getUserId());
        // refresh token 발급
        String refreshToken = tokenProvider.createRefreshToken(user.getUserId());

        // refresh 토큰 DB 저장
        UserRefreshToken userRefreshToken = new UserRefreshToken();
        userRefreshToken.setUserId(user);
        userRefreshToken.setRefreshToken(refreshToken);
        userRefreshTokenRepository.saveAndFlush(userRefreshToken);

        // 두가지 token 모두 반환
        List<String> token = new ArrayList<>();
        token.add(accessToken);
        token.add(refreshToken);

        return token;
    }
}
