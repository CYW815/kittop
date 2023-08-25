package com.kittop.config.auth;

import com.kittop.domain.UserVO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 스프링 시큐리티가 로그인 요청을 낚아채서 로그인을 진행시킴
 * 로그인을 진행이 완료가 되면 시큐리티 session을 만들어줌
 * Security ContextHolder 키 값에 에 저장시킴
 * 시큐리티에 들어갈 수 있는 세션의 객체는 지정 되어있음.
 * Authentication 타입의 오브젝트에 User 정보가 있어야 함.
 * User 객체의 타입은 UserDetails 타입 객체로 정해짐.
 * 즉 Security Session 에는 UserDetails 의 타입을 가진 User 를 가진
 * Authentication 오브젝트로 설정하면 스프링 시큐리티가 자동으로 처리
 *
 */
@Data
public class CustomUserDetails implements UserDetails, OAuth2User {

    private UserVO userVO;
    private Map<String, Object> attributes;

    /**
     * 일반 회원 로그인 시 사용될 생성자
     * @param userVO
     */
    public CustomUserDetails(UserVO userVO) {
        this.userVO = userVO;
    }

    /**
     * OAuth 로그인 시 사용될 생성자
     * @param userVO
     * @param attributes
     */
    public CustomUserDetails(UserVO userVO, Map<String, Object> attributes) {
        this.userVO = userVO;
        this.attributes = attributes;
    }

    /**
     * 해당 유저의 권한을 리턴
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(userVO.getRole());
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return userVO.getPassword();
    }

    @Override
    public String getUsername() {
        return userVO.getEmail();
    }

    public Long getUserId() {
        return userVO.getUserId();
    }

    /**
     * 계정 만료 여부 확인
     * true 는 만료 안됨
     * false 는 만료됨(인증 실패)
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부 확인
     * true 는 잠겨있지 않음
     * false 는 잠겨있음(인증 실패)
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 사용자 자격증명 만료 여부 확인
     * true 는 자격 증명이 만료되지 않음
     * false 는 자격 증명이 만료됨 (인증 실피)
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부 확인
     * true 는 계정 활성화 상태
     * false 는 계정 비활성화 상태
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Map<String, Object> getAttribute() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(userVO.getEmail());
    }

    public String getOAuth2UserPassword() {
        return String.valueOf(userVO.getPassword());
    }

    public String getOAuth2UserId() {
        return String.valueOf(userVO.getUserId());
    }

}
