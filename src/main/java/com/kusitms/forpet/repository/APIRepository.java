package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.placeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface APIRepository extends JpaRepository<placeInfo, Long> {

    List<placeInfo> findAllByCategory(String category);


}
