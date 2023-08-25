package com.kittop.config.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
public class CustomRememberMeAuthenticationFilter
        extends RememberMeAuthenticationFilter {

    public CustomRememberMeAuthenticationFilter(AuthenticationManager authenticationManager,
                                                RememberMeServices rememberMeServices) {
        super(authenticationManager, rememberMeServices);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Authentication authResult) {
        log.info("자동 로그인에 필요한 remember-me token 생성 성공!");

        super.onSuccessfulAuthentication(request, response, authResult);

    }
}
