package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.CommentComm;
import com.kusitms.forpet.domain.Community;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.CommunityDto;
import com.kusitms.forpet.repository.CommentCommRep;
import com.kusitms.forpet.repository.CommunityRep;
import com.kusitms.forpet.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentCommService {

    private final CommentCommRep commentCommRep;
    private final UserRep userRep;
    private final CommunityRep communityRep;

    /**
     * 댓글 생성
     */
    @Transactional
    public Long commentSave(Long userid, Long postId, String comment) {
        User user = userRep.findById(userid).get();
        Community community = communityRep.findById(postId).get();

        //댓글 생성
        CommentComm comentComm = CommentComm.createComentComm(comment, user, community, null);
        CommentComm save = commentCommRep.save(comentComm);

        return save.getId();
    }

    /**
     * 댓글 전체 조회
     * @return
     */
    @Transactional
    public List<CommunityDto.CommentParentResDto> getCommentList(Long postId) {
        List<CommentComm> list = commentCommRep.findByParent(postId);

        List<CommunityDto.CommentChildResDto> childList = new ArrayList<>();

        //entity -> dto 변환
        List<CommunityDto.CommentParentResDto> collect = list.stream().map(m -> new CommunityDto.CommentParentResDto(null, m.getId(), m.getContent(),
                m.getUser().getNickname(), m.getUser().getUserId(), m.getUser().getCustomImageUrl(), m.getCreateDate(), childList))
                .collect(Collectors.toList());

        return collect;
    }
}
