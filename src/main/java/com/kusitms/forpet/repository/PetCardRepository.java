package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.PetCard;
import com.kusitms.forpet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetCardRepository extends JpaRepository<PetCard, Long> {
    Optional<PetCard> findByUser(User user);
}
