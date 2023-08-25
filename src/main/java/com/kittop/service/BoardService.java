package com.kittop.service;


import com.kittop.dto.BoardDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;


public interface BoardService {

    void saveBoard(BoardDTO boardDTO);

    void saveReBoard(BoardDTO boardDTO);

    BoardDTO findBoardByBoardId(Long boardId);

    void updateBoard(BoardDTO boardDTO);

    BoardDTO findBoardbyOrderId(Long orderId);

    void deleteBoardByBoardId(Long boardId);

    //목록 검색기능
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    String getNow();



}
