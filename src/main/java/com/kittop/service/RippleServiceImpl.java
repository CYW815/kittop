package com.kittop.service;

import com.kittop.domain.RippleVO;
import com.kittop.dto.RippleDTO;
import com.kittop.mapper.RippleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class RippleServiceImpl implements RippleService{

    private final RippleMapper rippleMapper;
    private final ModelMapper modelMapper;
    @Override
    public void addRipple(RippleDTO rippleDTO) {
        rippleMapper.saveRipple(modelMapper.map(rippleDTO, RippleVO.class));
    }

    @Override
    public List<RippleDTO> findRippleByBoardId(long boardId) {
        return rippleMapper.findRippleByBoardId(boardId).stream().map(e -> modelMapper.map(e, RippleDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteRipple(long rippleId) {
        rippleMapper.deleteRipple(rippleId);
    }

    @Override
    public int findRippleCountByBoardId(long boardId) {
        return 0;
    }
}
