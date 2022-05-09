package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.placeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface APIRepository extends JpaRepository<placeInfo, Long> {

    List<placeInfo> findAllByCategory(String category);

    @Query(value =  "select * from place_info p where p.address like %:keyword% " +
            "or p.name like %:keyword% " +
            "or p.category like %:keyword%",
            nativeQuery = true)
    List<placeInfo> findByKeyword(String keyword);
}
