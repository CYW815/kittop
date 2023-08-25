package com.kittop.service;

import com.kittop.domain.RippleVO;
import com.kittop.dto.RippleDTO;

import java.util.List;

public interface RippleService {

    void addRipple(RippleDTO rippleDTO);

    List<RippleDTO> findRippleByBoardId(long boardId);

    void deleteRipple(long rippleId);

    int findRippleCountByBoardId(long boardId);
}
