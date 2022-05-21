package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.BookmarkComm;
import com.kusitms.forpet.domain.Community;
import com.kusitms.forpet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkCommRep extends JpaRepository<BookmarkComm, Long> {
    public Optional<BookmarkComm> findByCommunityAndUser(Community community, User user);
}