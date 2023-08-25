package com.kittop.mapper;

import com.kittop.domain.UserVO;
import com.kittop.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    //유저 등록
    void saveUser(UserVO userVO);

    //유저 삭제
    void deleteUser(Long userId);

    //유저 정보 수정
    void updateUser(UserVO userVO);

    //유저 아이디로 유저 찾기
    UserVO findUserByUserId(Long userId);

    //유저 이메일로 유저 찾기
    UserVO findUserByEmail(String email);

    //유저 닉네임으로 유저 찾기
    UserVO findUserByNickName(String nickName);

    //유저 리스트 불러오기
    List<UserVO> findUserList(PageRequestDTO pageRequestDTO);

    //전체 유저 수 가져오기
    int findUserCount(PageRequestDTO pageRequestDTO);

    //자동로그인 위한 쿠키 임의 문자열 uuid 저장
    void updateUuid(Long userId);

    //저장된 쿠키 임의 문자열 uuid 로 유저 찾기
    UserVO findUserByUuid(String uuid);





}
