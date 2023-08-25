package com.kittop.mapper;

import com.kittop.domain.ItemVO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ItemMapperTest {

    @Autowired
    ItemMapper itemMapper;

    @Test
    @DisplayName("saveItemTest 테스트")
    public void saveItemTest() {
        ItemVO itemVO = ItemVO.builder()
                .category("반팔티")
                .itemName("test1")
                .price(19800)
                .stock(100)
                .hit(0)
                .content("test1 입니다")
                .imgName("test01")
                .build();
        itemMapper.saveItem(itemVO);
        log.info(itemVO);

    }


}
