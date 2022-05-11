package com.kusitms.forpet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;

    private String nickname;

    private String phone;

    private String address1;
    private String address2;
    private String address3;

    //리뷰 참조관계
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Review> reviewList = new ArrayList<>();

    //북마크(offline-map) 참조관계
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarkList = new ArrayList<>();

    //북마크(QnaBoard) 참조관계
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BookmarkQna> bookmarkQnaList = new ArrayList<>();


    //백과사전 참조관계
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<QnaBoard> QnaBoardList = new ArrayList<>();


    @Builder(builderClassName= "social", builderMethodName = "socialBuilder")
    private User(String name, @Email String email, String imageUrl) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public void updateNameAndImage(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
