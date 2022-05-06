package com.kusitms.forpet.domain;

import com.kusitms.forpet.domain.placeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id",unique = true)
    private Long id;

    //@ManyToOne
    //@JoinColumn(name = "user_id")
    //private User user;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private placeInfo placeInfo;

    //==연관관계 메서드==//
    public void setPlaceInfo(placeInfo placeInfo) {
        this.placeInfo = placeInfo;
        placeInfo.getBookMarkList().add(this);
    }

}
