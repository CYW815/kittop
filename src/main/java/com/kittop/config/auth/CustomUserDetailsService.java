package com.kittop.config.auth;

import com.kittop.domain.UserVO;
import com.kittop.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 시큐리티 설정에서 loginProcessingUrl("/kittop/login")
 * 요청이 들어오면 자동으로 UserDetailsService 타입으로 지정된
 * loadUserByUsername() 메소드가 실행 됨
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    /**
     *
     * @param email the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserVO userVO = userMapper.findUserByEmail(email);

        if (userVO != null) {
            return new CustomUserDetails(userVO);
        }
        return null;

    }
}
