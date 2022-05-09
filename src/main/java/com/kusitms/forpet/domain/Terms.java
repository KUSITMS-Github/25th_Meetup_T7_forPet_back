package com.kusitms.forpet.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Terms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsId;

    private String name;

    private String content;

    @Column(length=1)
    private String is_required;

    @Builder
    public Terms(long termsId) {
        this.termsId = termsId;
    }
}
