package com.kittop.service;


import com.kittop.domain.BoardVO;
import com.kittop.dto.BoardDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    private final ModelMapper modelMapper;


    @Override
    public void saveBoard(BoardDTO boardDTO) {
        log.info(modelMapper);
        log.info("addBoard...");

        BoardVO boardVO = modelMapper.map(boardDTO, BoardVO.class);

        log.info(boardVO);

        boardMapper.saveBoard(boardVO);

    }

    @Override
    public void saveReBoard(BoardDTO boardDTO) {
        log.info(modelMapper);
        log.info("addBoard...");

        BoardVO boardVO = modelMapper.map(boardDTO, BoardVO.class);

        log.info(boardVO);

        boardMapper.saveReBoard(boardVO);

    }

    @Override
    public BoardDTO findBoardByBoardId(Long boardId) {
        log.info("readOne..." + boardId);
        BoardVO boardVO = boardMapper.findBoardByBoardId(boardId);
        log.info("readOne...: " + boardVO);
        BoardDTO boardDTO = modelMapper.map(boardVO ,BoardDTO.class);

        return boardDTO;
    }

    @Override
    public BoardDTO findBoardbyOrderId(Long orderId) {
        BoardVO boardVO = boardMapper.findBoardByOrderId(orderId);
        BoardDTO boardDTO = null;
        try {
            boardDTO = modelMapper.map(boardVO, BoardDTO.class);
        }catch (Exception e){
            return null;
        }


        return boardDTO;
    }

    @Override
    public void updateBoard(BoardDTO boardDTO) {
        log.info("board modify........");
        //vo객체를 생성하면서 매개변수로 받은 (값이 입력되어있는)dto와 vo를 맵핑(값을 넣음)해서 vo에 값을 입력
        log.info(modelMapper);
        BoardVO boardVO = modelMapper.map(boardDTO, BoardVO.class);
        log.info(boardVO);

        boardMapper.updateBoard(boardVO);

    }

    @Override
    public void deleteBoardByBoardId(Long boardId)
    {
        BoardVO boardVO = boardMapper.findBoardByBoardId(boardId);
        boardMapper.deleteBoardByBoardId(boardId);
        //boardId로 구해온 보드 데이터내의 작성자값과 새션 내의 유저값 비교



    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("service-list");
        //!!조건검색 쿼리로 변경
        //List<BoardVo> volist=boardMapper.selectAll();
        List<BoardVO> voList = boardMapper.list(pageRequestDTO);
        log.info(voList);
        List<BoardDTO> dtoList = new ArrayList<>();
        for(BoardVO boardVO : voList) {
            dtoList.add(modelMapper.map(boardVO, BoardDTO.class));
        }
        int total = boardMapper.getCount(pageRequestDTO);

        PageResponseDTO<BoardDTO> pageResponseDTO =  PageResponseDTO.<BoardDTO>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResponseDTO;
    }

    @Override
    public String getNow() {
        return boardMapper.getNow();
    }
}