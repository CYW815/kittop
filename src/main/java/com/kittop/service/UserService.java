package com.kittop.service;

import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.dto.UserDTO;

public interface UserService {

    /**
     * 회원 등록 기능
     * @param userDTO
     */
    public void saveUser(UserDTO userDTO);

//    public String authenticateEmail(String to) throws Exception;

    /**
     * 동일 email 이 있는지 검증 (Ajax)
     * @param email
     * @return
     */
    public boolean isEmailExists(String email);

    /**
     * 동일 nickName 이 있는지 검증 (Ajax)
     * @param nickName
     * @return
     */
    public boolean isNickNameExists(String nickName);

    /**
     * email 로 회원 조회 (로그인 기능)
     * @param email
     * @return
     */
    public UserDTO findUserByEmail(String email);

    /**
     * userId 로 회원 조회
     * @param userId
     * @return
     */
    public UserDTO findUserByUserId(Long userId);

    /**
     * 회원 수정 기능
     * @param userDTO
     */
    public void updateUser(UserDTO userDTO);

    /**
     * 회원 삭제 기능
     * @param userId
     */
    public void deleteUser(Long userId);

    /**
     * 회원 전체 리스트 조회(페이징, 검색)
     * @param pageRequestDTO
     * @return
     */
    PageResponseDTO<UserDTO> findUserList(PageRequestDTO pageRequestDTO);

}