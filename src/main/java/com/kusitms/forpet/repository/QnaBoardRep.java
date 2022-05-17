package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.QnaBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QnaBoardRep extends JpaRepository<QnaBoard, Long> {

    Page<QnaBoard> findAll(Pageable pageable);

    //검색 쿼리(페이징)
    @Query(value = "select * from qna_board q where q.title like %:keyword% or q.content like %:keyword%",
            nativeQuery = true)
    Page<QnaBoard> findAllSearch(String keyword, Pageable pageable);
}
