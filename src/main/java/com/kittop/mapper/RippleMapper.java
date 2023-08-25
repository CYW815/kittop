package com.kittop.mapper;

import com.kittop.domain.RippleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RippleMapper {

    // 리플 등록
    void saveRipple(RippleVO rippleVO);

    // 리플 삭제
    void deleteRipple(long rippleId);

    // boardId로 ripple 찾기
    List<RippleVO> findRippleByBoardId(long boardId);

}
