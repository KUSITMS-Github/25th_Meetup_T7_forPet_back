package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.BookmarkQna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface BookmarkQnaRep extends JpaRepository<BookmarkQna, Long> {

    //userid, boardId로 bookmark_qna 찾기
    @Query(value = "select * from bookmark_qna b where b.user_id = :userid and b.qna_id = :boardId" , nativeQuery = true)
    List<BookmarkQna> findByUserAndQna(Long userid, Long boardId);

    //북마크 취소
    @Modifying
    @Transactional
    @Query(value = "delete from bookmark_qna b where b.user_id = :userId and b.qna_id = :boardId", nativeQuery = true)
    void deleteBookmark(@Param("userId") Long userId, @Param("boardId") Long boardId);



}
