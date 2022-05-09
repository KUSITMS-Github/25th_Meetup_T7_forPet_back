package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.Bookmark;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query(value = "select * from bookmark b, place_info p " +
            "where b.place_id = p.place_id and p.category = :category", nativeQuery = true)
    List<Bookmark> find(String category);

    @Query(value = "select * from bookmark b where b.user_id = :userid", nativeQuery = true)
    List<Bookmark> findByUserId(Long userid);
}

