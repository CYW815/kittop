package com.kittop.mapper;

import com.kittop.domain.BoardVO;
import com.kittop.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    void saveBoard(BoardVO boardVO);

    void saveReBoard(BoardVO boardVO);

    String getNow();

    List<BoardVO> findBoardAll();

    BoardVO findBoardByBoardId(Long boardId);

    BoardVO findBoardByOrderId(Long orderId);

    void updateBoard(BoardVO boardVO);

    void deleteBoardByBoardId(Long boardId);

    List<BoardVO> list(PageRequestDTO pageRequestDTO);

    int getCount(PageRequestDTO pageRequestDTO);


}
