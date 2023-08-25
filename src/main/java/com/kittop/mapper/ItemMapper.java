package com.kittop.mapper;

import com.kittop.domain.ItemVO;
import com.kittop.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {

    // 아이템 등록
    void saveItem(ItemVO itemVO);

    // 아이템 업데이트
    void updateItem(ItemVO itemVO);

    void updateItemHitByItemId(long itemId);

    // 아이템 삭제
    void deleteItem(long itemId);

    // 선택 아이템 삭제
    void deleteSelectedItems(Long[] itemId);

    List<ItemVO> findItemAll();

    // 아이템 전체목록, 아이템 검색값으로 가져오기
    List<ItemVO> findItemByPageRequestDTO(PageRequestDTO pageRequestDTO);

    // 아이템 번호로 아이템 하나 가져오기
    ItemVO findItemByItemId(long itemId);

    // 전체 아이템 개수, 검색 아이템 개수
    int findItemCountByPageRequestDTO(PageRequestDTO pageRequestDTO);

    //카테고리로 아이템 리스트 조회
    List<ItemVO> findItemByCategory(String category);

}
