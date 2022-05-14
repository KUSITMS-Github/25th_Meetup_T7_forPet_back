package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.BookmarkQna;
import com.kusitms.forpet.domain.CommentQna;
import com.kusitms.forpet.domain.QnaBoard;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.ClickDto;
import com.kusitms.forpet.dto.QnaBoard.CommentQnaRespDto;
import com.kusitms.forpet.dto.QnaBoard.QnaBoardResponseDto;
import com.kusitms.forpet.repository.BookmarkQnaRep;
import com.kusitms.forpet.repository.CommentQnaRep;
import com.kusitms.forpet.repository.QnaBoardRep;
import com.kusitms.forpet.repository.UserRepository;
import com.kusitms.forpet.security.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaBoardRep qnaBoardRepository;
    private final UserRepository userRepository;
    private final BookmarkQnaRep bookmarkQnaRepository;
    private final CommentQnaRep commentQnaRep;
    private final S3Uploader s3Uploader;


    /**
     * qnaBoard 생성
     */
    @Transactional
    public Long createQnaBoard(Long userid,
                               String title, String content, List<String> hashTagList,
                               List<MultipartFile> multipartFiles) {
        //userid로 User 찾기
        User user = userRepository.findById(userid).get();

        //리뷰 이미지 s3 저장
        List<String> imageNameList = s3Uploader.uploadImage(multipartFiles);
        //리뷰 이미지 url로 변경
        StringBuilder imageUrlList = new StringBuilder();
        for (String imageName : imageNameList) {
            imageUrlList.append("https://kusitms-forpet.s3.ap-northeast-2.amazonaws.com/");
            imageUrlList.append(imageName);
            imageUrlList.append("#");
        }

        //해쉬태그 list -> string 변경
        StringBuilder hashTagList2 = new StringBuilder();
        for(String hashTag : hashTagList) {
            hashTagList2.append(hashTag);
            hashTagList2.append("/");
        }


        //qnaBoard 생성
        QnaBoard qnaBoard = QnaBoard.createQnaBoard(user, title, content, imageUrlList.toString(), hashTagList2.toString());
        QnaBoard save = qnaBoardRepository.save(qnaBoard);

        return save.getId();
    }


    /**
     * 게시글 조회
     * @param boardId
     */
    public QnaBoardResponseDto getBoardById(Long boardId) {
        QnaBoard qnaBoard = qnaBoardRepository.findById(boardId).get();

        QnaBoardResponseDto dto = null;

        if(qnaBoard.getUser().getRole().equals(Role.USER)){
            dto = new QnaBoardResponseDto(qnaBoard.getId(), "예비반려인", qnaBoard.getUser().getNickname(),
                    qnaBoard.getTitle(), qnaBoard.getContent(), qnaBoard.getCreateDate(),
                    qnaBoard.getLikes(), qnaBoard.getBookmarkQnaList().size(), qnaBoard.getCommentQnaList().size(),
                    qnaBoard.getImageUrlList().split("#"));
        }
        if(qnaBoard.getUser().getRole().equals(Role.FORPET_USER)){
            dto = new QnaBoardResponseDto(qnaBoard.getId(), "반려인", qnaBoard.getUser().getNickname(),
                    qnaBoard.getTitle(), qnaBoard.getContent(), qnaBoard.getCreateDate(),
                    qnaBoard.getLikes(), qnaBoard.getBookmarkQnaList().size(), qnaBoard.getCommentQnaList().size(),
                    qnaBoard.getImageUrlList().split("#"));
        }

        return dto;

    }


    @Data
    @AllArgsConstructor
    public static class Result<T> {
        private T qnaBoard;
        private T comment;
    }


    /**
     * qnaBoard 좋아요
     * @param board_id
     */
    @Transactional
    public int saveLikes(Long board_id) {
        QnaBoard qnaBoard = qnaBoardRepository.findById(board_id).get();
        qnaBoard.setLikes(qnaBoard.getLikes()+1);
        QnaBoard save = qnaBoardRepository.save(qnaBoard);

        return save.getLikes();
    }



    /**
     * qnaBoard 좋아요 취소
     * @param boardId
     */
    public int deleteLikes(Long boardId) {
        QnaBoard qnaBoard = qnaBoardRepository.findById(boardId).get();
        qnaBoard.setLikes(qnaBoard.getLikes()-1);
        QnaBoard save = qnaBoardRepository.save(qnaBoard);

        return save.getLikes();
    }



    /**
     * qnaBoard 북마크
     * @param userid
     * @param boardId
     */
    @Transactional
    public Map<String, Integer> createBookmark(Long userid, Long boardId) {
        User user = userRepository.findById(userid).get();
        QnaBoard qnaBoard = qnaBoardRepository.findById(boardId).get();

        BookmarkQna bookmarkQna = new BookmarkQna();
        bookmarkQna.setUser(user);
        bookmarkQna.setQnaBoard(qnaBoard);

        BookmarkQna save = bookmarkQnaRepository.save(bookmarkQna);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(save.getQnaBoard().getBookmarkQnaList().size());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        List<BookmarkQna> b = save.getQnaBoard().getBookmarkQnaList();
        for (BookmarkQna b1 : b) {
            System.out.println();
        }


        Map<String, Integer> map = new HashMap<>();

        map.put("id" , save.getId().intValue());
        map.put("cnt", save.getQnaBoard().getBookmarkQnaList().size()-1);


        return map;
    }


    /**
     * qnaBoard 북마크 취소
     * @param bookmarkId
     */
    public int deleteBookmark(Long bookmarkId) {
        BookmarkQna bookmarkQna = bookmarkQnaRepository.findById(bookmarkId).get();
        bookmarkQnaRepository.delete(bookmarkQna);

        List<BookmarkQna> b = bookmarkQna.getQnaBoard().getBookmarkQnaList();
        for (BookmarkQna b1 : b) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(b1.getId());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return bookmarkQna.getQnaBoard().getBookmarkQnaList().size();
    }


}
