package com.kusitms.forpet.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

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
    private double star;

    private String content;
    private String createDate;

    @Lob
    private String imageUrlList;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private placeInfo placeInfo;

    /**
     * User와 연관관계
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



    //==연관관계 메서드==//
    public void setPlaceInfo(placeInfo placeInfo) {
        this.placeInfo = placeInfo;
        placeInfo.getReviewList().add(this);
    }

    public void setUser(User user) {
        this.user = user;
        user.getReviewList().add(this);
    }


    //==LocalDateTime 커스텀==//
    public void setCreateDate(LocalDateTime localDateTime) {
        String month = String.valueOf(localDateTime.getMonthValue());
        String day = String.valueOf(localDateTime.getDayOfMonth());
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        String dayOfWeekKor = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREA);
        this.createDate = month + "." + day + " " + dayOfWeekKor;
    }



    //==생성 메서드==//
    public static Review createReview(User user,
                                      double star, String content, placeInfo placeInfo, String imageUrlList){
        Review review = new Review();
        review.setUser(user);
        review.setStar(star);
        review.setContent(content);
        review.setCreateDate(LocalDateTime.now());
        review.setPlaceInfo(placeInfo);
        review.setImageUrlList(imageUrlList);
        return review;
    }
}
