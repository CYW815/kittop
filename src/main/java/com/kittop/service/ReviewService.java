package com.kittop.service;

import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    void register(ReviewDTO reviewDTO);

    ReviewDTO readReview(Long reviewId);

//    void remove(Long reviewId);
    void remove(Long reviewId);

    PageResponseDTO<ReviewDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<ReviewDTO> findReviewByPageRequestDTO(PageRequestDTO pageRequestDTO);

    List<ReviewDTO> findReviewInfoByItemId(long itemId);

    ReviewDTO findReviewByOrderId(Long orderId);

}
