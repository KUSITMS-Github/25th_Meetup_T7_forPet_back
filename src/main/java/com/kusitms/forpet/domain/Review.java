package com.kusitms.forpet.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "review_id", unique = true)
    private Long id;

    @Column(nullable = false)
    private int star;

    private String content;
    private String writer;
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private placeInfo placeInfo;

    /**
     * User와 연관관계
     */
    //@ManyToOne
    //@JoinColumn(name = "user테이블의 primary key")
    //private User user;



    //==연관관계 메서드==//
    public void setPlaceInfo(placeInfo placeInfo) {
        this.placeInfo = placeInfo;
        placeInfo.getReviewList().add(this);
    }

    //==생성 메서드==//
    public static Review createReview(//Member member,
                                       int star, String content, String writer, placeInfo placeInfo){
        Review review = new Review();
        //review.setMember(member);
        review.setStar(star);
        review.setContent(content);
        review.setWriter(writer);
        review.setCreateDate(LocalDateTime.now());
        review.setPlaceInfo(placeInfo);
        return review;
    }
}
