package com.kittop.service;

import com.kittop.domain.ItemVO;
import com.kittop.dto.ItemDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final ModelMapper modelMapper;

    @Value("${item.path}")
    private String itemPath;


    /**
     * 아이템 등록 (이미지 파일 등록)
     * @param itemDTO
     * @param file
     * @throws IOException
     */
    @Override
    public void saveItem(ItemDTO itemDTO, MultipartFile file) throws IOException {
        log.info("service: saveItem ...");

        if(file != null && !file.isEmpty()) {
            File directory = new File(itemPath);
            if(!directory.exists()){
                directory.mkdirs();
            }
            String fileName = file.getOriginalFilename();
            file.transferTo(new File(itemPath, fileName));
            itemDTO.setImgName(fileName);
        }
        ItemVO itemVO = modelMapper.map(itemDTO, ItemVO.class);
        log.info(itemVO);
        itemMapper.saveItem(itemVO);
    }

    /**
     * 아이템 정보 조회
     * @param itemId
     * @return
     */
    @Override
    public ItemDTO findItemByItemId(Long itemId) {
        log.info("service: findItemByItemId...");

        ItemVO itemVO = itemMapper.findItemByItemId(itemId);
        ItemDTO itemDTO = modelMapper.map(itemVO, ItemDTO.class);

        log.info("service: " + itemDTO);
        return itemDTO;
    }

    @Override
    public List<ItemDTO> findItemAll() {
        log.info("service: findItemAll...");

        List<ItemVO> itemVOList = itemMapper.findItemAll();
        List<ItemDTO> itemDTOList = new ArrayList<>();

        for (ItemVO itemVO : itemVOList) {
            ItemDTO itemDTO = modelMapper.map(itemVO, ItemDTO.class);
            itemDTOList.add(itemDTO);
        }
        return itemDTOList;

    }

    @Override
    public PageResponseDTO<ItemDTO> findItemList(PageRequestDTO pageRequestDTO) {
        log.info("service: findItemList...");
        // 검색 결과를 포함한 페이지 정보 조회
        int total = itemMapper.findItemCountByPageRequestDTO(pageRequestDTO);
        List<ItemDTO> dtoList = itemMapper.findItemByPageRequestDTO(pageRequestDTO).stream().map(m -> modelMapper.map(m, ItemDTO.class)).collect(Collectors.toList());

        // PageResponseDTO 생성
        PageResponseDTO<ItemDTO> pageResponseDTO = PageResponseDTO.<ItemDTO>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();

        return pageResponseDTO;
    }

    @Override
    public void updateItem(ItemDTO itemDTO, MultipartFile file) throws IOException {

        if(file != null && !file.isEmpty()) {
            File directory = new File(itemPath);
            if(!directory.exists()){
                directory.mkdirs();
            }
            String fileName = file.getOriginalFilename();
            file.transferTo(new File(itemPath, fileName));
            itemDTO.setImgName(fileName);
        }
        itemMapper.updateItem(modelMapper.map(itemDTO, ItemVO.class));
    }

    //선택한 아이템 삭제
    @Override
    public void deleteSelectedItems(Long[] itemId) {
        log.info("deleteSelectedItems:"+itemId);
        String param = "";
        for(int i =0; i < itemId.length; i++){
            param += itemId[i];
            if(i < itemId.length-1){
                param += " ,";
            }
        }
        itemMapper.deleteSelectedItems(itemId);
        log.info(param);
    }

    /**
     * 조회 수 증가
     * @param itemId
     */
    @Override
    public void updateItemHitByItemId(Long itemId) {
        log.info("service : updateItemHitByItemId...");
        itemMapper.updateItemHitByItemId(itemId);
    }

    @Override
    public int findItemCountByPageRequestDTO(PageRequestDTO pageRequestDTO) {
        return itemMapper.findItemCountByPageRequestDTO(pageRequestDTO);
    }

}
