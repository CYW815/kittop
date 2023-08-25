package com.kittop.config.oauth;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.domain.Role;
import com.kittop.domain.UserVO;
import com.kittop.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserMapper userMapper;

    /**
     * 구글로그인 후 구글로 부터 받은 userRequest 데이터에 대한
     * 후처리 할 메소드
     * @param userRequest the user request
     * @return
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //registrationId 로 어떤 OAuth 로 로그인 했는지 확인 가능
        log.info("클라이언트 요청: " + userRequest.getClientRegistration());
        log.info("구글 엑세스 토큰: " + userRequest.getAccessToken().getTokenValue());
        log.info("구글 속성: " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getClientName();  //Google 반환
        String providerId = oAuth2User.getAttribute("sub");
        String gemail = oAuth2User.getAttribute("email");
        String gpassword = oAuth2User.getAttribute("sub");
        String gname = oAuth2User.getAttribute("name");

        UserVO userVO = userMapper.findUserByEmail(gemail);
        if (userVO == null) {
            log.info("해당 사용자는 구글 로그인이 최초입니다.");
            userVO = UserVO.builder()
                    .email(gemail)
                    .password(encoder.encode(gpassword))
                    .nickName(provider + "_" + providerId)
                    .name(gname)
                    .birth("0")
                    .gender("G")
                    .addr("0")
                    .phone(providerId)
                    .provider(provider)
                    .providerId(providerId)
//                    .uuid("0")
                    .role(Role.ROLE_GOOGLE)
                    .build();
            userMapper.saveUser(userVO);
            log.info("생성된 구글 계정 회원정보: " + userVO);
        }
        else {
            log.info("해당 사용자는 구글 로그인을 한적이 있습니다.자동 회원가입 상태입니다.");
            }

        /**
         * 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴
         * (OAuth-Client라이브러리) -> AccessToken 요청을 받는 것이 userRequest 정보
         * userRequest 정보 -> loadUser() 메소드 실행 -> 구글 회원 프로필을 받음
         */
        // OAuth2 로그인 시 사용될 UserDetails 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(userVO, oAuth2User.getAttributes());

        // CustomUserDetails 객체를 반환
        return userDetails;
    }
}
