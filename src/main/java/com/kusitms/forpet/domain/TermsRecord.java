package com.kusitms.forpet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TermsRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "terms_id")
    private Terms termsId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(length=1)
    private String is_agree;

    private LocalDateTime date;

    @Builder
    public TermsRecord(Terms terms, User user, String is_agree, LocalDateTime date) {
        Assert.notNull(terms, "terms must not be null");
        Assert.notNull(user, "user must not be null");
        Assert.hasText(is_agree, "is_agree must not be empty");
        Assert.notNull(date, "date must not be null");

        this.termsId = terms;
        this.userId = user;
        this.is_agree = is_agree;
        this.date = date;
    }
}
