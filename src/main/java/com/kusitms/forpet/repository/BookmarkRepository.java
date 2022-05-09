package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;


import com.kusitms.forpet.domain.Bookmark;
import com.kusitms.forpet.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query(value = "select bookmark_id, b.place_id from bookmark b, place_info p " +
            "where b.place_id = p.place_id and p.category = :category", nativeQuery = true)
    List<Bookmark> find(String category);
}

