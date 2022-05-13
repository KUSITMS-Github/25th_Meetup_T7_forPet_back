package com.kusitms.forpet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkQna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id",unique = true)
    private Long id;

    //회원 참조관계
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //백과사전 참조관계
    @ManyToOne
    @JoinColumn(name = "qna_id")
    private QnaBoard qnaBoard;


    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getBookmarkQnaList().add(this);
    }

    public void setQnaBoard(QnaBoard qnaBoard) {
        this.qnaBoard = qnaBoard;
        qnaBoard.getBookmarkQnaList().add(this);
    }


}
