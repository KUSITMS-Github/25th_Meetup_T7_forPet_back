package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.CommentQna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentQnaRep extends JpaRepository<CommentQna, Long> {

    @Query(value = "select * from comment_qna c where c.qna_id = :qnaId", nativeQuery = true)
    List<CommentQna> findAllByqnaBoard(Long qnaId);

    @Query(value = "select distinct c.qna_id from comment_qna c where c.user_id = :userid", nativeQuery = true)
    List<Long> find(Long userid);

}
