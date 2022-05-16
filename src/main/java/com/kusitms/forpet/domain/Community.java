package com.kusitms.forpet.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "community")
@NoArgsConstructor
@Getter
@DynamicInsert //null인 필드 값 insert 시 제와
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    /**
     * User와 연관관계
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String title;
    private String content;

    private LocalDateTime date;

    @ColumnDefault("0")
    private Long thumbsUpCnt;

    @Lob
    private String imageUrlList;

    private String category;

    private String address;

    @Builder
    private Community(User userId, String title, String content, String imageUrlList, String category, String address) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = LocalDateTime.now();
        this.imageUrlList = imageUrlList;
        this.category = category;
        this.address = address;
    }

    /**
    * 추천수 증가
     */
    public void updateThumbsUpCnt() {
        this.thumbsUpCnt++;
    }

    /**
     * 포스트 수정
     */
    public void update(String title, String content, String imageUrlList, String category, String address) {
        this.title = title;
        this.content = content;
        this.imageUrlList = imageUrlList;
        this.category = category;
        this.address = address;
    }
}