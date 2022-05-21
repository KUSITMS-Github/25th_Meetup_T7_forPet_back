package com.kusitms.forpet.service;


import com.kusitms.forpet.domain.CommentComm;
import com.kusitms.forpet.domain.Community;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.Community.CommentChildResDto;
import com.kusitms.forpet.dto.Community.CommentParentResDto;
import com.kusitms.forpet.repository.CommentCommRep;
import com.kusitms.forpet.repository.CommunityRepository;
import com.kusitms.forpet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentCommServ {

    private final CommentCommRep commentCommRep;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;


    // 부모 댓글 생성
    public Long saveParentComment(Long userid, Long postId, String comment) {
        User user = userRepository.findById(userid).get();
        Community community = communityRepository.findById(postId).get();

        CommentComm comm = CommentComm.createComentComm(comment, user, community, null);

        CommentComm save = commentCommRep.save(comm);

        return save.getId();
    }


    // 자식 댓글 생성
    public Long saveChildComment(Long userid, Long postId, Long parentCommentId, String comment) {
        User user = userRepository.findById(userid).get();
        Community community = communityRepository.findById(postId).get();
        CommentComm comm = commentCommRep.findById(parentCommentId).get();

        CommentComm comentComm = CommentComm.createComentComm(comment, user, community, comm);

        CommentComm save = commentCommRep.save(comentComm);

        return save.getId();
    }



    //댓글 전체 조회
    public List<CommentParentResDto> getComments(Long postId) {
        Community community = communityRepository.findById(postId).get();

        //부모 댓글 조회
        List<CommentComm> parentCommentList = commentCommRep.findByParent(postId);
        List<CommentParentResDto> collect = new ArrayList<>();
        List<CommentChildResDto> childCommentDtoList = new ArrayList<>();

        //entity -> dto 변환
        for(CommentComm parentComment : parentCommentList) {
            if(parentComment.getChildren() != null) {   //자식 댓글 있으면
                //Sus
                for(CommentComm childComment : parentComment.getChildren()) {
                    childCommentDtoList.add(new CommentChildResDto(childComment.getId(), childComment.getParent().getId(), childComment.getContent()));
                }
                collect.add(new CommentParentResDto(parentComment.getId(), parentComment.getUser().getImageUrl(), parentComment.getUser().getNickname(),
                        parentComment.getContent(), parentComment.getCreateDate(), parentComment.getLikes(), childCommentDtoList));
            } else {    //자식 댓글 없으면
                collect.add(new CommentParentResDto(parentComment.getId(), parentComment.getUser().getImageUrl(), parentComment.getUser().getNickname(),
                        parentComment.getContent(), parentComment.getCreateDate(), parentComment.getLikes(), null));
            }
        }


        return collect;
    }
}
