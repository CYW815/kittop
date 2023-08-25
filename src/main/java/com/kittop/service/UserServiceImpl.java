package com.kittop.service;

import com.kittop.domain.Role;
import com.kittop.domain.UserVO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.dto.UserDTO;
import com.kittop.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    HttpSession session;
    private JavaMailSender mailSender;

//    public static final String epw = createKey();

    @Override
    public void saveUser(UserDTO userDTO) {
        log.info("service: saveUser...");

        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        UserVO userVO = UserVO.createUser(userDTO);

        log.info("service:" + userVO);
        userMapper.saveUser(userVO);
    }

    /* 회원가입 시 이메일 중복검사 */
    @Override
    public boolean isEmailExists(String email) {
        log.info("service: isEmailExists..." );

        return userMapper.findUserByEmail(email) != null;
    }

    /* 회원가입 시 닉네임 중복검사 */
    @Override
    public boolean isNickNameExists(String nickName) {
        log.info("service: isNickNameExists");

        return userMapper.findUserByNickName(nickName) != null;
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        log.info("service: findUserByEmail...");

        UserVO userVO = userMapper.findUserByEmail(email);
        UserDTO userDTO = modelMapper.map(userVO, UserDTO.class);

        log.info("service: email 로 조회된 회원 정보 " + userDTO);
        return userDTO;
    }

    @Override
    public UserDTO findUserByUserId(Long userId) {
        log.info("service: findUserByUserId");

        UserVO userVO = userMapper.findUserByUserId(userId);
        UserDTO userDTO = modelMapper.map(userVO, UserDTO.class);

        log.info("service: 고유번호로 조회된 회원 정보 " + userDTO);
        return userDTO;
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        log.info("service: updateUser");
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        UserVO userVO = modelMapper.map(userDTO, UserVO.class);
        userMapper.updateUser(userVO);
        log.info("회원 수정 정보: " + userVO);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("service: deleteUser");

        userMapper.deleteUser(userId);
    }

    @Override
    public PageResponseDTO<UserDTO> findUserList(PageRequestDTO pageRequestDTO) {
        log.info("service-userlist");

        List<UserVO> voList = userMapper.findUserList(pageRequestDTO);
        log.info(voList);
        List<UserDTO> dtoList = new ArrayList<>();
        for(UserVO userVO : voList) {
            dtoList.add(modelMapper.map(userVO,UserDTO.class));
        }
        int total = userMapper.findUserCount(pageRequestDTO);

        PageResponseDTO<UserDTO> pageResponseDTO =
                PageResponseDTO.<UserDTO>withAll()
                        .dtoList(dtoList)
                        .total(total)
                        .pageRequestDTO(pageRequestDTO)
                        .build();
        return pageResponseDTO;
    }
}

