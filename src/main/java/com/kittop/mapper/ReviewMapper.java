package com.kittop.mapper;

import com.kittop.domain.ReviewVO;
import com.kittop.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {

    // 리뷰 등록
    void saveReview(ReviewVO reviewVO);

    String getNow();

    // 리뷰 삭제
//    void deleteReview(Long reviewId);
    void deleteReviewByReviewId(Long reviewId);

    // itemId로 리뷰 찾기
    List<ReviewVO> findReviewByPageRequestDTO(PageRequestDTO pageRequestDTO);

    List<ReviewVO> list(PageRequestDTO pageRequestDTO);

    List<ReviewVO> findReviewInfoByItemId(long itemId);

    // reviewId로 리뷰 찾기
    ReviewVO findReviewByReviewId(long reviewId);

    // 페이징 처리를 위한 리뷰 개수 가져오기
    int findReviewCount(PageRequestDTO pageRequestDTO);

    ReviewVO findReviewByOrderId(Long orderId);



}
