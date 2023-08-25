package com.kittop.service;

import com.kittop.domain.ItemVO;
import com.kittop.dto.CartDTO;
import com.kittop.mapper.ItemMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class CartServiceTests {

    @Autowired
    private CartService cartService;
    @Autowired
    private ItemMapper itemMapper;


    @Test
    public void selectCartTests() {
        List<CartDTO> cartDTO = cartService.findCartByUserEmail("cho0815y@gmail.com");
        for(CartDTO c : cartDTO){
            c.setItem(itemMapper.findItemByItemId(c.getItemId()));
        }
        cartDTO.forEach(log::info);
    }

    @Test
    public void saveItem() {
        for (int i = 101; i < 201; i++) {
            itemMapper.saveItem(ItemVO.builder()
                    .itemName("테스트" + i)
                    .content("test" + i)
                    .price(i * 10000)
                    .stock(i * 100)
                    .category("긴팔셔츠")
                    .imgName("item006.jpg")
                    .build());
        }
    }

    @Test
    public void tests() {
        String addr = "28562/충북 청주시 서원구 1순환로 627 (사창동)/123";
        log.info(addr.substring(0, addr.lastIndexOf(" ")));
    }
}
