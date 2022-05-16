package com.kusitms.forpet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private User user;

    private String title;
    private String content;

    private LocalDateTime date;

    @Lob
    private String imageUrlList;

    private String category;

    private String address;

    @Builder
    private Community(User user, String title, String content, String imageUrlList, String category, String address) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.date = LocalDateTime.now();
        this.imageUrlList = imageUrlList;
        this.category = category;
        this.address = address;
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

    // 북마크(Community) 참조 관계
    @JsonIgnore
    @OneToMany(mappedBy = "community")
    private List<BookmarkComm> bookmarkCommList = new ArrayList<>();

    // 좋아요(Community 참조 관계)
    @JsonIgnore
    @OneToMany(mappedBy = "community")
    private List<LikesComm> likesCommList = new ArrayList<>();
}