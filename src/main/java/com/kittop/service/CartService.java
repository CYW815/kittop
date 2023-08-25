package com.kittop.service;

import com.kittop.dto.CartDTO;

import java.util.List;

public interface CartService {

    void saveCart(long itemId) throws Exception;

    void deleteCart(long cartId);

    void deleteCartByUserEmail(String userEmail, long itemId);

    List<CartDTO> findCartByUserEmail(String userEmail);

    int findCartCountByUserEmail(String userEmail);
}
