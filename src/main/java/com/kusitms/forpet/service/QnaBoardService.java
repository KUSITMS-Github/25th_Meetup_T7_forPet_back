package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.BookmarkQna;
import com.kusitms.forpet.domain.QnaBoard;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.repository.BookmarkQnaRep;
import com.kusitms.forpet.repository.CommentQnaRep;
import com.kusitms.forpet.repository.QnaBoardRep;
import com.kusitms.forpet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

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
     * qnaBoard 북마크
     * @param userid
     * @param boardId
     */
    @Transactional
    public int createBookmark(Long userid, Long boardId) {
        User user = userRepository.findById(userid).get();
        QnaBoard qnaBoard = qnaBoardRepository.findById(boardId).get();

        BookmarkQna bookmarkQna = new BookmarkQna();
        bookmarkQna.setUser(user);
        bookmarkQna.setQnaBoard(qnaBoard);
        BookmarkQna save = bookmarkQnaRepository.save(bookmarkQna);

        return save.getQnaBoard().getBookmarkQnaList().size();
    }


    /*
    @Transactional
    public Result getQnaBoardByLatest(Pageable pageable) {

        Page<QnaBoard> list = qnaBoardRepository.findAll(pageable);

        int max = 0;
        Long commentId = 0L;
        List<QnaBoardResponseDto> collect = new ArrayList<>();

        for(QnaBoard m : list) {
            System.out.println("QnaBoard:>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(m.getId());
            //댓글 중 좋아요 수 최대 찾기
            List<CommentQna> commentQnaList = m.getCommentQnaList();
            System.out.println("commentQnaList:>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(commentQnaList.size());
            if(commentQnaList.size() != null) {
            for(CommentQna commentQna : commentQnaList) {
                if(max <= commentQna.getLikes()) {
                    max = commentQna.getLikes();
                    System.out.println("max:>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.out.println(max);
                    commentId = commentQna.getId();
                    System.out.println("commentid:>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.out.println(commentId);
                }
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(commentId);

            CommentQna c = commentQnaRep.findById(commentId).get();

            collect.add(new QnaBoardResponseDto(m.getId(),
                            //m.getUser().getTag,
                            m.getUser().getNickname(),
                            m.getTitle(), m.getContent(), m.getCreateDate(),
                            m.getLikes(), m.getBookmarkQnaList().size(), m.getCommentQnaList().size(),
                            m.getImageUrlList().split("#"),
                            new CommentQnaRespDto(c.getId(), c.getUser().getImageUrl(), c.getUser().getNickname(),
                            //c.getUser.getTag,
                            c.getComment(), c.getCreateDate(), c.getLikes())));

        }


        return new Result(list.getNumber(), list.getNumberOfElements(), list.getTotalPages(), list.getTotalElements(), collect);

    }


    //리턴값
    @Data
    @AllArgsConstructor
    public static class Result<T> {
        private int number;             //페이지 번호
        private int numberOfElement;    //페이지 요소 개수
        private int totalPages;         //전체 페이지 개수
        private Long totalElements;     //잔체 요소 개수
        private T data;
    }

     */


}
