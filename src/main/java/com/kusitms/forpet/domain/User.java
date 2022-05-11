package com.kusitms.forpet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kusitms.forpet.dto.SignUpDto;
import com.kusitms.forpet.security.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@NoArgsConstructor
@Getter

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

    // 회원가입으로 받게 되는 추가 정보
    private String nickname;
    private String phone;
    private String address1;
    private String address2;
    private String address3;
    private String customImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    //리뷰 참조관계
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Review> reviewList = new ArrayList<>();

    //북마크 참조관계
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarkList = new ArrayList<>();


    @Builder(builderClassName= "social", builderMethodName = "socialBuilder")
    private User(String name, @Email String email, String imageUrl, Role role) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    @Builder
    private User(Long userId) {
        this.userId = userId;
    }

    @Builder(builderClassName= "role", builderMethodName = "roleBuilder")
    private User(Role role) {
        this.role = role;
    }

    public void updateNameAndImage(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    // 권한 업데이트
    public void updateRole(Role role) {
        this.role = role;
    }

    // 커스텀 프로필 이미지 업데이트
    public void updateCustomImage(String customImageUrl) {
        this.customImageUrl = customImageUrl;
    }

    // 회원가입
    public void signupUser(String nickname, String phone, SignUpDto.Address address) {
        this.nickname = nickname;
        this.phone = phone;
        this.address1 = address.getAddr1();
        this.address2 = address.getAddr2();
        this.address3 = address.getAddr3();
        // 회원
        this.role = Role.USER;
    }
}
