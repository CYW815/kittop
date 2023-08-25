package com.kittop.service;

import com.kittop.dto.ItemDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    //아이템 등록(이미지 파일 등록)
    void saveItem(ItemDTO itemDTO, MultipartFile file) throws IOException;

    //아이템 번호로 아이템 하나 가져오기
    ItemDTO findItemByItemId(Long itemId);

    //아이템 전체 목록 가져오기
    List<ItemDTO> findItemAll();

    //아이템 페이징 처리
    PageResponseDTO<ItemDTO> findItemList(PageRequestDTO pageRequestDTO);

    //아이템 수정
    void updateItem(ItemDTO itemDTO, MultipartFile file) throws IOException;

    //선택한 아이템 삭제
    public void deleteSelectedItems(Long[] itemId);

    //조회수 증가
    void updateItemHitByItemId(Long itemId);

    int findItemCountByPageRequestDTO(PageRequestDTO pageRequestDTO);

}
