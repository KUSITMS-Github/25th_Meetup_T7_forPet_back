package com.kusitms.forpet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentQna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id",unique = true)
    private Long id;

    private String comment;             //댓글 내용
    private LocalDateTime createDate;
    private int likes;                  //좋아요 수

    //연관관계
    @ManyToOne
    @JoinColumn(name = "qna_id")
    private QnaBoard qnaBoard;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    //== 연관관계 메서드 ==//
    public void setUser(User user){
        this.user = user;
        user.getCommentQnaList().add(this);
    }

    public void setQnaBoard(QnaBoard qnaBoard) {
        this.qnaBoard = qnaBoard;
        qnaBoard.getCommentQnaList().add(this);
    }

    //== 생성 메서드 ==//
    public static CommentQna createCommentQna(User user, QnaBoard qnaBoard, String comment) {
        CommentQna commentQna = new CommentQna();
        commentQna.setUser(user);
        commentQna.setQnaBoard(qnaBoard);
        commentQna.setComment(comment);
        commentQna.setCreateDate(LocalDateTime.now());

        return commentQna;
    }
}
