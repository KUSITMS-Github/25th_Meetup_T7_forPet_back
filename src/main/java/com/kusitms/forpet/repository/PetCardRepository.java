package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.PetCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetCardRepository extends JpaRepository<PetCard, Long> {
}
