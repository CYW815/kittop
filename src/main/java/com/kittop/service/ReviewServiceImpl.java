package com.kittop.service;

import com.kittop.domain.ReviewVO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.dto.ReviewDTO;
import com.kittop.mapper.ItemMapper;
import com.kittop.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final ModelMapper modelMapper;
    private final ItemMapper itemMapper;

    /*리뷰 등록*/
    @Override
    public void register(ReviewDTO reviewDTO) {
        log.info(modelMapper);

        ReviewVO reviewVO = modelMapper.map(reviewDTO, ReviewVO.class);

        log.info(reviewVO);
        reviewMapper.saveReview(reviewVO);
    }

    /*리뷰 보기*/
    @Override
    public ReviewDTO readReview(Long reviewId) {
        log.info("readReview" + reviewId);
        ReviewVO reviewVO = reviewMapper.findReviewByReviewId(reviewId);
        log.info(reviewVO);
        ReviewDTO reviewDTO = modelMapper.map(reviewVO, ReviewDTO.class);
        reviewDTO.setItem(itemMapper.findItemByItemId(reviewDTO.getItemId()));
        return reviewDTO;
    }

    /*리뷰 삭제*/
    @Override
    public void remove(Long reviewId) {
        ReviewVO reviewVO = reviewMapper.findReviewByReviewId(reviewId);
        reviewMapper.deleteReviewByReviewId(reviewId);

    }

    @Override
    public ReviewDTO findReviewByOrderId(Long orderId) {
        ReviewVO reviewVO = reviewMapper.findReviewByOrderId(orderId);
        ReviewDTO reviewDTO = null;
        try {
            reviewDTO = modelMapper.map(reviewVO, ReviewDTO.class);
        }catch (Exception e){
            return null;
        }

        return reviewDTO;
    }

    /*리뷰 리스트*/
    @Override
    public PageResponseDTO<ReviewDTO> list(PageRequestDTO pageRequestDTO) {
        List<ReviewVO> voList = reviewMapper.list(pageRequestDTO);

        List<ReviewDTO> dtoList = new ArrayList<>();
        for (ReviewVO reviewVO : voList){
            dtoList.add(modelMapper.map(reviewVO, ReviewDTO.class));
        }
        int total = reviewMapper.findReviewCount(pageRequestDTO);

        PageResponseDTO<ReviewDTO> pageResponseDTO = PageResponseDTO.<ReviewDTO>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResponseDTO;
    }

    @Override
    public PageResponseDTO<ReviewDTO> findReviewByPageRequestDTO(PageRequestDTO pageRequestDTO) {
        log.info("ReviewList,,,");
        List<ReviewDTO> voList = reviewMapper.findReviewByPageRequestDTO(pageRequestDTO).stream()
                .map(dto -> modelMapper.map(dto, ReviewDTO.class)).collect(Collectors.toList());
        log.info(voList);

        for(ReviewDTO dtoList : voList){
            dtoList.setItem(itemMapper.findItemByItemId(dtoList.getItemId()));
        }
        int total = reviewMapper.findReviewCount(pageRequestDTO);

        PageResponseDTO<ReviewDTO> pageResponseDTO = new PageResponseDTO<>(pageRequestDTO, voList, total);
        return pageResponseDTO;
    }

    @Override
    public List<ReviewDTO> findReviewInfoByItemId(long itemId) {
        List<ReviewDTO> reviewDTO = reviewMapper.findReviewInfoByItemId(itemId).stream().map(m -> modelMapper.map(m , ReviewDTO.class)).collect(Collectors.toList());
        reviewDTO.forEach(m -> m.setItem(itemMapper.findItemByItemId(m.getItemId())));
        return reviewDTO;
    }

//    @Override
//    public PageResponseDTO<ReviewDTO> findReviewByPageRequestDTO(PageRequestDTO pageRequestDTO) {
//        log.info("ReviewList,,,");
//        List<ReviewVO> voList = reviewMapper.findReviewByPageRequestDTO(pageRequestDTO);
//        log.info(voList);
//
//        List<ReviewDTO> dtoList = new ArrayList<>();
//
//        for (ReviewVO reviewVO : voList){
//            dtoList.add(modelMapper.map(reviewVO, ReviewDTO.class));
//            log.info(reviewVO + ".....");
//        }
////        for (ReviewVO reviewVO : voList){
////            ReviewDTO dto = modelMapper.map(reviewVO, ReviewDTO.class);
//////            dto.setItemId(reviewVO.getItemId());
////            dtoList.add(dto);
////            log.info(dto + ",,");
////        }
//        int total = reviewMapper.findReviewCount(pageRequestDTO);
//
//        PageResponseDTO<ReviewDTO> pageResponseDTO = PageResponseDTO.<ReviewDTO>withAll()
//                .dtoList(dtoList)
//                .total(total)
//                .pageRequestDTO(pageRequestDTO)
//                .build();
//        return pageResponseDTO;
//    }

//    @Override
//    public PageResponseDTO<ReviewDTO> findReviewByPageRequestDTO(PageRequestDTO pageRequestDTO) {
//        log.info("ReviewList,,,");
//        List<ReviewVO> voList = reviewMapper.findReviewByPageRequestDTO(pageRequestDTO);
//        log.info(voList);
//
//        List<ReviewDTO> dtoList = voList.stream()
//                .map(reviewVO -> modelMapper.map(reviewVO, ReviewDTO.class))
//                .collect(Collectors.toList());
//
//        int total = reviewMapper.findReviewCount(pageRequestDTO);
//
//        PageResponseDTO<ReviewDTO> pageResponseDTO = new PageResponseDTO<>(pageRequestDTO, dtoList, total);
//        return pageResponseDTO;
//    }
}
