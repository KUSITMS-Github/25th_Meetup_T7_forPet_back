package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.*;
import com.kusitms.forpet.dto.CommunityDto;
import com.kusitms.forpet.exception.CustomException;
import com.kusitms.forpet.repository.BookmarkCommRepository;
import com.kusitms.forpet.repository.CommunityRepository;
import com.kusitms.forpet.repository.LikesCommRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.kusitms.forpet.dto.response.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final S3Uploader s3Uploader;
    private final CommunityRepository communityRepository;
    private final LikesCommRepository likesCommRepository;
    private final BookmarkCommRepository bookmarkCommRepository;
    private final static String NO_ADDRESS = "x";

    /**
     * 인기 포스트(주소 개수)
     */
    public List<Community> findOrderByThumbsUpAndAddress(String[] addressList) {
        if(addressList.length == 1) {
            return communityRepository.findOrderByThumbsUpCntAndAddress(
                    addressList[0], NO_ADDRESS, NO_ADDRESS);
        } else if(addressList.length == 2) {
            return communityRepository.findOrderByThumbsUpCntAndAddress(
                    addressList[0], addressList[1], NO_ADDRESS);
        } else {
            return communityRepository.findOrderByThumbsUpCntAndAddress(
                    addressList[0], addressList[1], addressList[2]);
        }

   }

    /**
     * 카테고리 포스트 - 페이지네이션 필요 없음.
     */
    public List<Community> findByCategoryAndAddress(String category, String[] addressList) {
        if(addressList.length == 1) {
            return communityRepository.findByCategoryAndAddress(category,
                    addressList[0], NO_ADDRESS, NO_ADDRESS);
        } else if(addressList.length == 2) {
            return communityRepository.findByCategoryAndAddress(category,
                    addressList[0], addressList[1], NO_ADDRESS);
        } else {
            return communityRepository.findByCategoryAndAddress(category,
                    addressList[0], addressList[1], addressList[2]);
        }
    }

    /**
     * 카테고리 포스트 - 페이지네이션
     */
    public List<Community> findByCategoryAndAddress(String category, String[] addressList, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if(addressList.length == 1) {
            return communityRepository.findByCategoryAndAddress(category,
                    addressList[0], NO_ADDRESS, NO_ADDRESS, pageable);
        } else if(addressList.length == 2) {
            return communityRepository.findByCategoryAndAddress(category,
                    addressList[0], addressList[1], NO_ADDRESS, pageable);
        } else {
            return communityRepository.findByCategoryAndAddress(category,
                    addressList[0], addressList[1], addressList[2], pageable);
        }
    }

    /* 댓글수
    public Long findNumberOfComment(Long postId) {

    }
     */

    /**
    * postId로 가져오기
     */
    public Community findCommunityById(Long postId) {
        Optional<Community> communityOptional = communityRepository.findById(postId);
        if(communityOptional.isPresent()) {
            return communityOptional.get();
        }
        return null;
    }
    /**
     *
     * @param keyword
     * @param addressList
     * @param page
     * @param size
     * @return
     */
    public List<Community> findByKeyword(String keyword, String[] addressList, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if(addressList.length == 1) {
            return communityRepository.findByKeyword(keyword,
                    addressList[0], NO_ADDRESS, NO_ADDRESS, pageable);
        } else if(addressList.length == 2) {
            return communityRepository.findByKeyword(keyword,
                    addressList[0], addressList[1], NO_ADDRESS, pageable);
        } else {
            return communityRepository.findByKeyword(keyword,
                    addressList[0], addressList[1], addressList[2], pageable);
        }
    }

    @Transactional
    public Long createPost(User user,
                           CommunityDto.CommunityRequest requestDto,
                           List<MultipartFile> multipartFiles) {

        //포스트 이미지 s3 저장
        List<String> imageNameList = s3Uploader.uploadImage(multipartFiles);
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        System.out.println(imageNameList);
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        //포스트 이미지 url로 변경
        StringBuilder imageUrlList = new StringBuilder();
        for (String imageName : imageNameList) {
            imageUrlList.append("https://kusitms-forpet.s3.ap-northeast-2.amazonaws.com/");
            imageUrlList.append(imageName);
            imageUrlList.append("#");
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        System.out.println(imageUrlList);
        System.out.println(">>>>>>>>>>>>>>>>>>>>");

        // 포스트 저장
        Community newComm = Community.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageUrlList(imageUrlList.toString())
                .category(requestDto.getCategory())
                .address(user.getAddress())
                .build();

        communityRepository.saveAndFlush(newComm);

        return newComm.getPostId();
    }

    @Transactional
    public Long updatePost(Long postId,
                           User user,
                           CommunityDto.CommunityRequest requestDto,
                           List<MultipartFile> multipartFiles)
    {
        //포스트 이미지 s3 저장
        List<String> imageNameList = s3Uploader.uploadImage(multipartFiles);
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        System.out.println(imageNameList);
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        //포스트 이미지 url로 변경
        StringBuilder imageUrlList = new StringBuilder();
        for (String imageName : imageNameList) {
            imageUrlList.append("https://kusitms-forpet.s3.ap-northeast-2.amazonaws.com/");
            imageUrlList.append(imageName);
            imageUrlList.append("#");
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        System.out.println(imageUrlList);
        System.out.println(">>>>>>>>>>>>>>>>>>>>");


        // 포스트 수정
        Optional<Community> community = communityRepository.findById(postId);
        if(community.isPresent()) {
            community.get().update(requestDto.getTitle(), requestDto.getContent(), imageUrlList.toString(), requestDto.getCategory(), user.getAddress());
            communityRepository.saveAndFlush(community.get());
        }
        return community.get().getPostId();
    }

    public Long deletePost(Long postId) {
        communityRepository.deleteById(postId);
        return postId;
    }

    /**
     * 게시글 좋아요
     */
    @Transactional
    public int saveLikes(User user, Long postId) {
        Optional<Community> comm = communityRepository.findById(postId);
        Community community = null;
        LikesComm likesComm = null;


        if(comm.isPresent()) {
            community = comm.get();

            Optional<LikesComm> likesCommOptional = likesCommRepository.findByCommunityAndUser(community, user);
            if(likesCommOptional.isPresent()) {
                // 이미 좋아요한 경우
                throw new CustomException(CANNOT_DUPLICATE_LIKE);
            }

            likesComm = new LikesComm();
            likesComm.setUser(user);
            likesComm.setCommunity(community);

            likesCommRepository.save(likesComm);
        }

        return likesComm.getCommunity().getLikesCommList().size()-1;
    }

    /**
     * 게시글 좋아요 취소
     */
    @Transactional
    public int deleteLikes(User user, Long postId) {
        Optional<Community> comm = communityRepository.findById(postId);
        Community community = null;
        LikesComm likesComm = null;
        if(comm.isPresent()) {
            community = comm.get();

            likesComm = likesCommRepository.findByCommunityAndUser(community, user).get();
            likesComm.getCommunity().getLikesCommList().remove(likesComm);

            communityRepository.save(community);
            likesCommRepository.delete(likesComm);
        }

        return likesComm.getCommunity().getLikesCommList().size();
    }

    /**
     * 좋아요 여부 확인( 좋아요 했다면 true)
     */
    @Transactional
    public boolean getLikes(User user, Community community) {
        Optional<LikesComm> likesComm = likesCommRepository.findByCommunityAndUser(community, user);
            if(likesComm.isPresent()) {
                return true;
            }
        return false;
    }

    /**
     * 게시글 북마크
     */
    @Transactional
    public int saveBookMark(User user, Long postId) {
        Optional<Community> comm = communityRepository.findById(postId);
        Community community = null;
        BookmarkComm bookmarkComm = null;
        if(comm.isPresent()) {
            community = comm.get();

            bookmarkComm = new BookmarkComm();
            bookmarkComm.setUser(user);
            bookmarkComm.setCommunity(community);

            bookmarkCommRepository.save(bookmarkComm);
        }

        return bookmarkComm.getCommunity().getBookmarkCommList().size()-1;
    }

    /**
     * 게시글 좋아요 취소
     */
    @Transactional
    public int deleteBookMark(User user, Long postId) {
        Optional<Community> comm = communityRepository.findById(postId);
        Community community = null;
        BookmarkComm bookmarkComm = null;
        if(comm.isPresent()) {
            community = comm.get();

            bookmarkComm = bookmarkCommRepository.findByCommunityAndUser(community, user).get();
            bookmarkComm.getCommunity().getBookmarkCommList().remove(bookmarkComm);

            communityRepository.save(community);
            bookmarkCommRepository.delete(bookmarkComm);
        }

        return bookmarkComm.getCommunity().getBookmarkCommList().size();
    }

    /**
     * 좋아요 여부 확인( 좋아요 했다면 true)
     */
    @Transactional
    public boolean getBookMark(User user, Community community) {
        Optional<BookmarkComm> bookmarkComm = bookmarkCommRepository.findByCommunityAndUser(community, user);
        if(bookmarkComm.isPresent()) {
            return true;
        }
        return false;
    }
}
