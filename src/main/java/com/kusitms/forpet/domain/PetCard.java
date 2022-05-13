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

    @ManyToOne
    @JoinColumn(name = "user_id", unique=true)
    private User userId;

    private String cardNumber;

    private String imageUrl;

    @Builder
    public PetCard(User userId, String cardNumber, String imageUrl) {
        Assert.notNull(userId, "userId must not be null");
        Assert.hasText(cardNumber, "cardNumber must not be empty");
        Assert.hasText(imageUrl, "imageUrl must not be empty");

        this.userId = userId;
        this.cardNumber = cardNumber;
        this.imageUrl = imageUrl;
    }
}
