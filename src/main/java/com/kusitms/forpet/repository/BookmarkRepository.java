package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
