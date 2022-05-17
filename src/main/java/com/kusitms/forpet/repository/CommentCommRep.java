package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.CommentComm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentCommRep extends JpaRepository<CommentComm, Long> {

    //부모 댓글 찾기
    @Query(value = "select * from comment_comm c where c.post_id = :postId and c.parent_id is null", nativeQuery = true)
    List<CommentComm> findByParent(Long postId);

    //자식 댓글 찾기
    @Query(value = "select * from comment_comm c where c.post_id = :postId and c.parend_id is not null", nativeQuery = true)
    List<CommentComm> findByChild(Long postId);
}
