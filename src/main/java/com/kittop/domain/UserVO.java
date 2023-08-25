package com.kittop.domain;

import com.kittop.dto.UserDTO;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Log4j2
public class UserVO {

    private Long userId;
    private String email;
    private String password;
    private String nickName;
    private String name;
    private String birth;
    private String gender;    //'M','W'
    private String addr;
    private String phone;
    
    private String provider;
    private String providerId;
    
    private Timestamp regDate;
    private Timestamp updateDate;
    private String uuid;
    private Role role;  //ADMIN,USER


    /**
     * OAuthUser 생성을 위한 생성자
     * @param email
     * @param password
     * @param nickName
     * @param name
     * @param birth
     * @param gender
     * @param addr
     * @param phone
     * @param provider
     * @param providerId
     * @param role
     */
    @Builder
    public UserVO(String email, String password, String nickName, String name, String birth, String gender, String addr, String phone, String provider, String providerId, Role role) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.addr = addr;
        this.phone = phone;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
    }

    /**
     * UserVO 객체 생성 메소드
     * 패스워드 암호화 처리
     * 계정에 role(ADMIN, USER) 설정 처리
     * @param userDTO
     * @return
     */
    public static UserVO createUser(UserDTO userDTO) {
        UserVO userVO = UserVO.builder()
                .userId(userDTO.getUserId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .nickName(userDTO.getNickName())
                .name(userDTO.getName())
                .birth(userDTO.getBirth())
                .gender(userDTO.getGender())
                .addr(userDTO.getAddr())
                .phone(userDTO.getPhone())
                .uuid(userDTO.getUuid())
                .role(Role.ROLE_USER)
                .build();
        log.info("createUser:" + userVO);
        return userVO;
    }
}
