package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.Review;
import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.repository.APIRepository;
import com.kusitms.forpet.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final APIRepository apiRepository;

    /**
     * 리뷰 생성
     */
    @Transactional
    public Long createReviewByPlaceId(Long placeid, int star, String content, String writer) {
        placeInfo placeInfo = apiRepository.findById(placeid).get();

        //리뷰 생성
        Review review = Review.createReview(//Member member,
                            star, content, writer, placeInfo);
        // 별점수, 리뷰수 업데이트
        placeInfo placeInfo1 = placeInfo.setStarAvgAndReviewCnt(placeInfo);

        //리뷰 저장
        reviewRepository.save(review);
        apiRepository.save(placeInfo1);

        return review.getId();
    }


    public List<Review> findReviewByPlaceId(Long placeid) {
        return reviewRepository.findByplaceInfo(placeid);
    }
}
