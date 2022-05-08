package com.kusitms.forpet.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Terms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsId;

    private String name;

    private String content;

    @Column(length=1)
    private String is_required;
}
