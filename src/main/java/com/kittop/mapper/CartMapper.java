package com.kittop.mapper;

import com.kittop.domain.CartVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    // 카트 저장
    void saveCart(CartVO cartVO) throws Exception;

    //카트 삭제
    void deleteCart(long cartId);

    void deleteCartByUserEmail(String userEmail, long itemId);

    //해당 유저 카트 불러오기
    List<CartVO> findCartByUserEmail(String userEmail);

    int findCartCountByUserEmail(String userEmail);

}
