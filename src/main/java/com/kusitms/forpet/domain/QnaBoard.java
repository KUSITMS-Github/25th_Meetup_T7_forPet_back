package com.kusitms.forpet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QnaBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id",unique = true)
    private Long id;
    private String title;
    private String content;

    @Lob
    private String imageUrlList;

    private String hashTag;          //해시태그 정보
    private String createDate;
    private int likes;               //좋아요 개수


    //연관관계
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //백과사전 북마크 연관관계
    @JsonIgnore
    @OneToMany(mappedBy = "qnaBoard")
    private List<BookmarkQna> bookmarkQnaList = new ArrayList<>();

    //백과사전 댓글 연관관계
    @JsonIgnore
    @OneToMany(mappedBy = "qnaBoard")
    private List<CommentQna> commentQnaList = new ArrayList<>();



    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getQnaBoardList().add(this);
    }


    //==LocalDateTime 커스텀==//
    public void setCreateDate(LocalDateTime localDateTime) {
        //월, 일
        String month = String.valueOf(localDateTime.getMonthValue());
        String day = String.valueOf(localDateTime.getDayOfMonth());
        //시, 분
        String hour = String.valueOf(localDateTime.getHour());
        String min = String.format("%02d", localDateTime.getMinute());
        this.createDate = month + "/" + day + " " + hour + ":" + min;
    }


    //==생성 메서드==//
    public static QnaBoard createQnaBoard(User user,
                                      String title, String content, String imageUrlList, String hashTag){
        QnaBoard qnaBoard = new QnaBoard();
        qnaBoard.setTitle(title);
        qnaBoard.setContent(content);
        qnaBoard.setImageUrlList(imageUrlList);
        qnaBoard.setHashTag(hashTag);
        qnaBoard.setCreateDate(LocalDateTime.now());
        qnaBoard.setUser(user);
        return qnaBoard;
    }

}
