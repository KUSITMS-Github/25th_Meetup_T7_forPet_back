package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.QnaBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaBoardRep extends JpaRepository<QnaBoard, Long> {

    Page<QnaBoard> findAll(Pageable pageable);
}
