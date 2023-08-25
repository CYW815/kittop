package com.kittop.controller;

import com.kittop.dto.RippleDTO;
import com.kittop.service.RippleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/kittop/ripple/*")
public class RippleController {

    private final RippleService rippleService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> addRipple(@RequestBody RippleDTO ripple) {
        log.info(ripple);
        Map<String, String> map = new HashMap<>();
        try{
            rippleService.addRipple(ripple);
            map.put("result", "댓글 작성 완료");
        }catch (Exception e){
            map.put("result", "댓글 작성 오류");
        }
        return map;
    }

    @GetMapping("/{boardId}")
    public List<RippleDTO> rippleList(@PathVariable("boardId") long boardId) {
        List<RippleDTO> rippleDTOS = rippleService.findRippleByBoardId(boardId);
        return rippleDTOS;
    }

    @DeleteMapping("/{rippleId}")
    public Map<String, String> deleteRipple(@PathVariable("rippleId") long rippleId){
        Map<String, String> map = new HashMap<>();
        try{
            rippleService.deleteRipple(rippleId);
            map.put("result", "댓글이 정상적으로 삭제되었습니다");
        }catch (Exception e){
            map.put("result", "댓글 삭제중 오류 발생");
        }
        return map;
    }


}
