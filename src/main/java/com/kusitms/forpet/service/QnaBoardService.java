package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.BookmarkQna;
import com.kusitms.forpet.domain.QnaBoard;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.QnaBoard.QnaBoardResByIdDto;
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
                               String title, String content,
                               List<MultipartFile> multipartFiles) {
        //userid로 User 찾기
        User user = userRepository.findById(userid).get();

        QnaBoard qnaBoard = null;
        //리뷰 이미지 s3 저장
        if(multipartFiles != null) {
            List<String> imageNameList = s3Uploader.uploadImage(multipartFiles);
            //리뷰 이미지 url로 변경
            StringBuilder imageUrlList = new StringBuilder();
            for (String imageName : imageNameList) {
                imageUrlList.append("https://kusitms-forpet.s3.ap-northeast-2.amazonaws.com/");
                imageUrlList.append(imageName);
                imageUrlList.append("#");
            }
            //qnaBoard 생성
            qnaBoard= QnaBoard.createQnaBoard(user, title, content, imageUrlList.toString(), null);
        } else {
            qnaBoard = QnaBoard.createQnaBoard(user, title, content, null, null);
        }

        QnaBoard save = qnaBoardRepository.save(qnaBoard);

        return save.getId();
    }


    /**
     * 게시글 조회
     * @param boardId
     */
    public QnaBoardResByIdDto getBoardById(Long userid, Long boardId) {
        QnaBoard qnaBoard = qnaBoardRepository.findById(boardId).get();

        // 접속한 사용자가 북마크 했는지 여부 판단
        boolean toggle = false;
        List<BookmarkQna> byUserAndQna = bookmarkQnaRepository.findByUserAndQna(userid, boardId);
        if (byUserAndQna != null) {
            toggle = true;
        }

        QnaBoardResByIdDto dto = null;

        if(qnaBoard.getUser().getRole().equals(Role.USER)){
            if(qnaBoard.getImageUrlList() != null) {
                dto = new QnaBoardResByIdDto(qnaBoard.getId(), toggle, "예비반려인", qnaBoard.getUser().getNickname(),
                        qnaBoard.getTitle(), qnaBoard.getContent(), qnaBoard.getCreateDate(),
                        qnaBoard.getLikes(), qnaBoard.getBookmarkQnaList().size(), qnaBoard.getCommentQnaList().size(),
                        qnaBoard.getImageUrlList().split("#"));
            } else {
                dto = new QnaBoardResByIdDto(qnaBoard.getId(), toggle, "예비반려인", qnaBoard.getUser().getNickname(),
                        qnaBoard.getTitle(), qnaBoard.getContent(), qnaBoard.getCreateDate(),
                        qnaBoard.getLikes(), qnaBoard.getBookmarkQnaList().size(), qnaBoard.getCommentQnaList().size(),
                        null);
            }

        }
        if(qnaBoard.getUser().getRole().equals(Role.FORPET_USER)){
            if(qnaBoard.getImageUrlList() != null) {
                dto = new QnaBoardResByIdDto(qnaBoard.getId(),toggle, "반려인", qnaBoard.getUser().getNickname(),
                        qnaBoard.getTitle(), qnaBoard.getContent(), qnaBoard.getCreateDate(),
                        qnaBoard.getLikes(), qnaBoard.getBookmarkQnaList().size(), qnaBoard.getCommentQnaList().size(),
                        qnaBoard.getImageUrlList().split("#"));
            } else {
                dto = new QnaBoardResByIdDto(qnaBoard.getId(), toggle,"반려인", qnaBoard.getUser().getNickname(),
                        qnaBoard.getTitle(), qnaBoard.getContent(), qnaBoard.getCreateDate(),
                        qnaBoard.getLikes(), qnaBoard.getBookmarkQnaList().size(), qnaBoard.getCommentQnaList().size(),
                        null);
            }

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

        //중복 북마크 방지
        if(isNotAlreadyLike(userid, boardId)) {
            BookmarkQna save = bookmarkQnaRepository.save(bookmarkQna);
            Map<String, Integer> map = new HashMap<>();
            map.put("id" , save.getId().intValue());
            map.put("cnt", save.getQnaBoard().getBookmarkQnaList().size());

            return map;

        } else
            return null;
    }

    //사용자가 이미 북마크 한 게시물인지 체크
    private boolean isNotAlreadyLike(Long userid, Long boardId) {
        return bookmarkQnaRepository.findByUserAndQna(userid, boardId).isEmpty();
    }


    /**
     * qnaBoard 북마크 취소
     * @param bookmarkId
     */
    public int deleteBookmark(Long bookmarkId) {
        BookmarkQna bookmarkQna = bookmarkQnaRepository.findById(bookmarkId).get();
        bookmarkQnaRepository.delete(bookmarkQna);

        return bookmarkQna.getQnaBoard().getBookmarkQnaList().size();
    }


}
