package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.Review;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.dto.ReviewDto;
import com.kusitms.forpet.repository.APIRep;
import com.kusitms.forpet.repository.ReviewRep;
import com.kusitms.forpet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRep reviewRepository;
    private final APIRep apiRepository;
    private final S3Uploader s3Uploader;
    private final UserRepository userRepository;

    /**
     * 리뷰 생성
     */
    @Transactional
    public Long createReviewByPlaceId(Long placeid, Long userid, int star, String content,  List<MultipartFile> multipartFiles) {
        placeInfo placeInfo = apiRepository.findById(placeid).get();
        User user = userRepository.findById(userid).get();

        Review review = null;

        if(multipartFiles != null) {
            //리뷰 이미지 s3 저장
            List<String> imageNameList = s3Uploader.uploadImage(multipartFiles);
            System.out.println(">>>>>>>>>>>>>>>>>>>>");
            System.out.println(imageNameList);
            System.out.println(">>>>>>>>>>>>>>>>>>>>");
            //리뷰 이미지 url로 변경
            StringBuilder imageUrlList = new StringBuilder();
            for (String imageName : imageNameList) {
                imageUrlList.append("https://kusitms-forpet.s3.ap-northeast-2.amazonaws.com/");
                imageUrlList.append(imageName);
                imageUrlList.append("#");
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>");
            System.out.println(imageUrlList);
            System.out.println(">>>>>>>>>>>>>>>>>>>>");

            //리뷰 생성
            review = Review.createReview(user,star, content,placeInfo, imageUrlList.toString());
        } else {
            review = Review.createReview(user,star, content,placeInfo, null);
        }

        // 별점수, 리뷰수 업데이트
        placeInfo = placeInfo.setStarAvgAndReviewCnt(placeInfo);

        //리뷰 저장
        reviewRepository.save(review);
        apiRepository.save(placeInfo);

        return review.getId();
    }


    /**
     * 리뷰 정보
     * @param placeid
     */
    public List<ReviewDto> findReviewByPlaceId(Long placeid) {
        List<Review> list = reviewRepository.findByplaceInfo(placeid);

        List<ReviewDto> collect = new ArrayList<>();

        //entity -> dto 변환
        for(Review r : list) {
            if(r.getImageUrlList() != null) {
                collect.add(new ReviewDto(r.getId(), r.getUser().getNickname(), r.getUser().getImageUrl(), r.getStar(), r.getContent(),
                        r.getCreateDate(), r.getImageUrlList().split("#")));
            } else{
                collect.add(new ReviewDto(r.getId(), r.getUser().getNickname(), r.getUser().getImageUrl(), r.getStar(), r.getContent(),
                        r.getCreateDate(), null));
            }
        }

        return collect;
    }


}

