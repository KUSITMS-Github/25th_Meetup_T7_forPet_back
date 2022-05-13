package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.*;
import com.kusitms.forpet.dto.MyPage.BookmarkOfflineDto;
import com.kusitms.forpet.dto.MyPage.HistoryBoardDTO;
import com.kusitms.forpet.repository.CommentQnaRep;
import com.kusitms.forpet.repository.QnaBoardRep;
import com.kusitms.forpet.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final CommentQnaRep commentQnaRep;
    private final QnaBoardRep qnaBoardRep;


    /**
     * 마이페이지 히스토리 내가 쓴 글(커뮤니티, 백과사전)
     * @param userid
     */
    public Result getBoard(Long userid) {

        User user = userRepository.findById(userid).get();

        //List<Bookmark> CommunityList = user.getCommunityList();
        List<QnaBoard> QnaList = user.getQnaBoardList();

        //entity -> dto 변환 (커뮤니티)
        //List<Community> CommunityCollect = CommunityList.stream().map(m -> new HistoryBoardDTO(m.getId(), "커뮤니티 - " + m.getCategory(),  m.getTitle(), m.getLikes(), 댓글+대댓글))
        //.collect(Collectors.toList());

        //entity -> dto 변환 (퍼펫트 백과사전)
        List<HistoryBoardDTO> QnaCollect = QnaList.stream().map(m -> new HistoryBoardDTO(m.getId(), "퍼펫트 백과" , m.getTitle(), m.getLikes(), m.getCommentQnaList().size()))
                .collect(Collectors.toList());

        //return new Result(CommunityCollect, QnaCollect);
        return new Result(1, 2);
    }



    /**
     * 마이페이지 히스토리 답변한 글(커뮤니티, 백과사전)
     * 커뮤니티 댓글+대댓글 추가
     * @param userid
     */
    public List<HistoryBoardDTO> getBoardByComment(Long userid) {

        List<Long> qnaIdList = commentQnaRep.find(userid);

        List<HistoryBoardDTO> qnaCollect = new ArrayList<>();

        for(Long qnaId : qnaIdList) {
            QnaBoard qnaBoard = qnaBoardRep.findById(qnaId).get();
            qnaCollect.add(new HistoryBoardDTO(qnaBoard.getId(), "퍼펫트 백과" ,
                    qnaBoard.getTitle(), qnaBoard.getLikes(), qnaBoard.getCommentQnaList().size()));
        }

        return qnaCollect;
    }



    /**
     * 마이페이지 북마크(오프라인 지도)
     * @param userid
     */
    public List<BookmarkOfflineDto> getBookmarkOfflineMap(Long userid) {
        User user = userRepository.findById(userid).get();
        List<Bookmark> list = user.getBookmarkList();

        //entity -> dto 변환
        List<BookmarkOfflineDto> collect = list.stream().map(m -> new BookmarkOfflineDto(m.getPlaceInfo().getId(), m.getPlaceInfo().getCategory(),
                m.getPlaceInfo().getName(), m.getPlaceInfo().getAddress(), m.getPlaceInfo().getStarAvg(), m.getPlaceInfo().getReviewCnt()))
                .collect(Collectors.toList());

        return collect;
    }


    /**
     * 마이페이지 북마크(커뮤니티, 백과사전)
     * @param userid
     */
    public Result getBookmarkBoard(Long userid) {

        User user = userRepository.findById(userid).get();

        //List<BookmarkCommunity> bookmarkCommunityList = user.getBookmarkCommunityList();
        List<BookmarkQna> bookmarkQnaList = user.getBookmarkQnaList();

        //entity -> dto 변환 (커뮤니티)
        //bookmarkCommunityList.stream.map(m -> new HistoryBoardDTO(m.getCommunity().getId(), "커뮤니티 - " + m.getCategory(),
                        //m.getCommunity().getTitle(), m.getCommunity().getLikes(), 댓글+대댓글);

        //entity -> dto 변환 (퍼펫트 백과사전)
        List<HistoryBoardDTO> QnaCollect = bookmarkQnaList.stream().map(m -> new HistoryBoardDTO(m.getQnaBoard().getId(), "퍼펫트 백과" ,
                        m.getQnaBoard().getTitle(), m.getQnaBoard().getLikes(), m.getQnaBoard().getCommentQnaList().size()))
                .collect(Collectors.toList());

        //return new Result<, QnaCollect>();
        return new Result(1,1);
    }



    @Data
    @AllArgsConstructor
    public static class Result<T> {
        private T community;
        private T qnaBoard;
    }
}
