package com.kusitms.forpet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentComm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id",unique = true)
    private Long id;

    private String content;
    private String createDate;
    private int likes;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CommentComm parent;

    @OneToMany(mappedBy = "parent")
    private List<CommentComm> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Community community;


    //==LocalDateTime 커스텀==//
    public void setCreateDate(LocalDateTime localDateTime) {
        //월, 일
        String month = String.valueOf(localDateTime.getMonthValue());
        String day = String.valueOf(localDateTime.getDayOfMonth());
        //시, 분
        String hour = String.valueOf(localDateTime.getHour());
        String min = String.format("%02d", localDateTime.getMinute());
        this.createDate = month + "/" + day + " " + hour + ":" + min;
    }


    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getCommentCommList().add(this);
    }

    public void setCommunity(Community community) {
        this.community = community;
        community.getCommentCommList().add(this);
    }

    public void setParent(CommentComm parent) {
        this.parent = parent;
        parent.getChildren().add(this);
    }


    public static CommentComm createComentComm(String comment, User user, Community community, CommentComm parent) {
        CommentComm comm = new CommentComm();
        comm.setContent(comment);
        comm.setCreateDate(LocalDateTime.now());
        comm.setUser(user);
        comm.setCommunity(community);

        if(parent != null)
            comm.setParent(parent);     //자식 댓글이면

        return comm;
    }
}
