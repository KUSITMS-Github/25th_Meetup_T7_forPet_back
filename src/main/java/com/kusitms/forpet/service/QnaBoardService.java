package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.BookmarkQna;
import com.kusitms.forpet.domain.QnaBoard;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.repository.BookmarkQnaRep;
import com.kusitms.forpet.repository.QnaBoardRep;
import com.kusitms.forpet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaBoardRep qnaBoardRepository;
    private final UserRepository userRepository;
    private final BookmarkQnaRep bookmarkQnaRepository;
    private final S3Uploader s3Uploader;

    /**
     * qnaBoard 생성
     */
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
     * qnaBoard 좋아요
     * @param board_id
     */
    public int saveLikes(Long board_id) {
        QnaBoard qnaBoard = qnaBoardRepository.findById(board_id).get();
        qnaBoard.setLikes(qnaBoard.getLikes()+1);
        QnaBoard save = qnaBoardRepository.save(qnaBoard);

        return save.getLikes();
    }



    /**
     * qnaBoard 북마크
     * @param userid
     * @param boardId
     */
    public int createBookmark(Long userid, Long boardId) {
        User user = userRepository.findById(userid).get();
        QnaBoard qnaBoard = qnaBoardRepository.findById(boardId).get();

        BookmarkQna bookmarkQna = new BookmarkQna();
        bookmarkQna.setUser(user);
        bookmarkQna.setQnaBoard(qnaBoard);
        BookmarkQna save = bookmarkQnaRepository.save(bookmarkQna);

        return save.getQnaBoard().getBookmarkQnaList().size();
    }
}
