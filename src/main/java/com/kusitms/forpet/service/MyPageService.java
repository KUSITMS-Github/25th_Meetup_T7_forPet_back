package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.*;
import com.kusitms.forpet.dto.MyPage.BookmarkOfflineDto;
import com.kusitms.forpet.dto.MyPage.HistoryBoardDTO;
import com.kusitms.forpet.dto.MyPage.UserDetailDto;
import com.kusitms.forpet.dto.MyPage.UserUpdateDto;
import com.kusitms.forpet.repository.CommentQnaRep;
import com.kusitms.forpet.repository.PetCardRepository;
import com.kusitms.forpet.repository.QnaBoardRep;
import com.kusitms.forpet.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final PetCardRepository petCardRepository;
    private final UserRepository userRepository;
    private final CommentQnaRep commentQnaRep;
    private final QnaBoardRep qnaBoardRep;
    private final S3Uploader s3Uploader;


    /**
     * 마이페이지 사용자 정보 조회
     */
    public UserDetailDto getUser(Long userId) {
        User user = userRepository.findByUserId(userId);

        // 프로필 사진
        String profileImage;
        if(user.getCustomImageUrl() != null) {
            profileImage = user.getCustomImageUrl();
        } else {
            profileImage = user.getImageUrl();
        }

        // 주소 인증 여부
        boolean isCertifiedAddress = false;
        if(user.getAddress() != null) {
            isCertifiedAddress = true;
        }

        Optional<PetCard> petcard = petCardRepository.findByUser(user);
        // 동물 등록 카드 여부
        boolean isCertifiedPetCard = false;
        if(petcard.isPresent()) {
            isCertifiedPetCard = true;
        }

        UserDetailDto userDetailDto = new UserDetailDto(user.getUserId(), isCertifiedAddress, isCertifiedPetCard, user.getAddress().split("#"), user.getNickname(), user.getName(), profileImage);

        return userDetailDto;
    }
    /**
     * 마이페이지 사용자 정보 수정
     */
    public UserDetailDto updateUser(Long userId, UserUpdateDto dto, MultipartFile profileImage) {
        User user = userRepository.findByUserId(userId);

        // 프로필 사진
        if(!profileImage.getOriginalFilename().equals("")) {
            // image file -> url
            String profileImageName = s3Uploader.uploadImage(profileImage);
            StringBuilder profileImageUrl = new StringBuilder();
            profileImageUrl.append("https://kusitms-forpet.s3.ap-northeast-2.amazonaws.com/");
            profileImageUrl.append(profileImageName);

            user.updateCustomImage(profileImageUrl.toString());
        }

        // 닉네임과 주소 업데이트
        /**
         * null 처리해주어야 함!
         */
        user.updateNickname(dto.getNickname());
        user.updateAddress(dto.getAddress().getAddressList());

        userRepository.save(user);

        // 프로필 사진
        String profileImageDto;
        if(user.getCustomImageUrl() != null) {
            profileImageDto = user.getCustomImageUrl();
        } else {
            profileImageDto = user.getImageUrl();
        }

        // 주소 인증 여부
        boolean isCertifiedAddress = false;
        if(user.getAddress() != null) {
            isCertifiedAddress = true;
        }

        // 동물 등록 카드 여부
        Optional<PetCard> petcard = petCardRepository.findByUser(user);
        // 동물 등록 카드 여부
        boolean isCertifiedPetCard = false;
        if(petcard.isPresent()) {
            isCertifiedPetCard = true;
        }

        UserDetailDto userDetailDto = new UserDetailDto(user.getUserId(), isCertifiedAddress, isCertifiedPetCard, user.getAddress().split("#"), user.getNickname(), user.getName(), profileImageDto);
        return userDetailDto;
    }
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
    public List<HistoryBoardDTO> getBookmarkBoard(Long userid) {

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
        //return new Result(1,1);

        return QnaCollect;
    }



    @Data
    @AllArgsConstructor
    public static class Result<T> {
        private T community;
        private T qnaBoard;
    }
}
