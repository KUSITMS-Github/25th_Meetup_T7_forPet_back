package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.Community;
import com.kusitms.forpet.domain.LikesComm;
import com.kusitms.forpet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesCommRep extends JpaRepository<LikesComm, Long> {
    public Optional<LikesComm> findByCommunityAndUser(Community community, User user);
}