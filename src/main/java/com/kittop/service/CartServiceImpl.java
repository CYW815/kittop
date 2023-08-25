package com.kittop.service;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.domain.CartVO;
import com.kittop.domain.UserVO;
import com.kittop.dto.CartDTO;
import com.kittop.mapper.CartMapper;
import com.kittop.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartMapper cartMapper;
    private final ItemMapper itemMapper;
    private final ModelMapper modelMapper;

    @Override
    public void saveCart(long itemId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("service : saveCart ...");
        log.info("service: 현재 인증된 사용자 : " + authentication.getName());

        CartVO cartVO = CartVO.builder()
                        .itemId(itemId)
                        .userEmail(authentication.getName())
                        .build();
        cartMapper.saveCart(cartVO);
    }

    @Override
    public void deleteCart(long cartId) {
        cartMapper.deleteCart(cartId);
    }

    @Override
    public void deleteCartByUserEmail(String userEmail, long itemId) {
        cartMapper.deleteCartByUserEmail(userEmail, itemId);
    }

    @Override
    public List<CartDTO> findCartByUserEmail(String userEmail) {
        List<CartDTO> cartDTO = cartMapper.findCartByUserEmail(userEmail).stream()
                .map(dto -> modelMapper.map(dto, CartDTO.class)).collect(Collectors.toList());
        for(CartDTO c : cartDTO){
            c.setItem(itemMapper.findItemByItemId(c.getItemId()));
        }
        return cartDTO;
    }

    @Override
    public int findCartCountByUserEmail(String userEmail) {
        return cartMapper.findCartCountByUserEmail(userEmail);
    }
}
