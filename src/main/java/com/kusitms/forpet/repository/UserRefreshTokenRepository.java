package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    UserRefreshToken findByUserId(User userId);
    UserRefreshToken findByUserIdAndRefreshToken(User userId, String refreshToken);
}
