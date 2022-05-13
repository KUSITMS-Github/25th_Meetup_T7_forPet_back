package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.CommentQna;
import com.kusitms.forpet.domain.QnaBoard;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.QnaBoard.CommentQnaRespDto;
import com.kusitms.forpet.repository.CommentQnaRep;
import com.kusitms.forpet.repository.QnaBoardRep;
import com.kusitms.forpet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentQnaService {

    private final CommentQnaRep commentQnaRep;
    private final UserRepository userRepository;
    private final QnaBoardRep qnaBoardRep;


    /**
     * 댓글 생성
     * @param userid
     * @param boardId
     * @param comment
     */
    @Transactional
    public Long commentSave(Long userid, Long boardId, String comment) {
        User user = userRepository.findById(userid).get();
        QnaBoard qnaBoard = qnaBoardRep.findById(boardId).get();

        //댓글 생성
        CommentQna commentQna = CommentQna.createCommentQna(user, qnaBoard, comment);
        CommentQna save = commentQnaRep.save(commentQna);

        return save.getId();
    }


    /**
     * 백과사전 게시글별 전체 댓글 조회
     * @param boardId
     */
    public List<CommentQnaRespDto> getCommentList(Long boardId) {
        List<CommentQna> list = commentQnaRep.findAllByqnaBoard(boardId);

        //entity -> dto 변환
        List<CommentQnaRespDto> collect = list.stream().map(m -> new CommentQnaRespDto(m.getId(),
                        m.getUser().getImageUrl(), m.getUser().getNickname(),
                        //m.getUser().getTag(),
                        m.getComment(), m.getCreateDate(), m.getLikes()))
                .collect(Collectors.toList());

        return collect;

    }


    /**
     * 댓글 좋아요
     * @param commentId
     * @return
     */
    @Transactional
    public int saveLikes(Long commentId) {
        CommentQna commentQna = commentQnaRep.findById(commentId).get();
        commentQna.setLikes(commentQna.getLikes()+1);
        CommentQna save = commentQnaRep.save(commentQna);

        return save.getLikes();
    }
}
