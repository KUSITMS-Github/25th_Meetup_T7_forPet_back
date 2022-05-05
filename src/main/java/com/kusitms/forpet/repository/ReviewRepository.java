package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select * from review r where r.place_id = :placeid", nativeQuery = true)
    List<Review> findByplaceInfo(Long placeid);

}
