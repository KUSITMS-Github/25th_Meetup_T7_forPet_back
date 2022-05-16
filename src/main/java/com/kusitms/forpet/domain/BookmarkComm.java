package com.kusitms.forpet.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "bookmark_comm")
@NoArgsConstructor
@Getter
public class BookmarkComm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id",unique = true)
    private Long id;

    //회원 참조관계
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //커뮤니티 참조관계
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Community community;

    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getBookmarkCommList().add(this);
    }

    public void setCommunity(Community community) {
        this.community = community;
        community.getBookmarkCommList().add(this);
    }
}