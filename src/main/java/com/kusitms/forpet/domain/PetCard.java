package com.kusitms.forpet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PetCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @OneToOne
    @JoinColumn(name = "user_id", unique=true)
    private User user;


    private String imageUrl;

    @Builder
    public PetCard(User user, String imageUrl) {
        Assert.notNull(user, "userId must not be null");
        Assert.hasText(imageUrl, "imageUrl must not be empty");

        this.user = user;
        this.imageUrl = imageUrl;
    }
}
