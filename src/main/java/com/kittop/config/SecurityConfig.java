package com.kittop.config;

import com.kittop.config.auth.CustomUserDetailsService;
import com.kittop.config.oauth.CustomOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Value("${security.rememberme.key}")
//    private String rememberMeKey;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomOauth2UserService customOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        /**
         * 회원 등록 된 사용자
         * 로그인 페이지 및 처리 기능에 대한 설정
         */
        http.formLogin()
                //로그인 페이지 경로(GET 요청)
                .loginPage("/kittop/login")
                .permitAll()
                //로그인 성공 시 자동 이동 경로
                .defaultSuccessUrl("/", true)
                //로그인 시 인증에 사용 될 파라미터
                .usernameParameter("email")
                .passwordParameter("password")
                //로그인 실패시 이동할 경로
                .failureUrl("/kittop/login")
                .and()
                .logout()
                //로그아웃 경로(GET 요청)
                .logoutRequestMatcher(new AntPathRequestMatcher("/kittop/logout"))
                //로그아웃 후 자동 이동 경로
                .logoutSuccessUrl("/");

        /**
         * 특정 권한 인증이 있어야 접근 가능한 경로 설정
         * 인증 없이 접근 가능한 경로 설정
         * 그 외 경로는 그냥 인증 되면 접근 가능 설정
         */
        http.authorizeRequests()
                //"/admin"으로 시작되는 경로는 ADMIN Role을 가진 경우만 접근 가능 설정
                .antMatchers("/kittop/admin/**").hasRole("ADMIN")
                //permitAll() 을 통해 인증(로그인)없이 해당 경로에 접근 경로 설정
                .mvcMatchers("/", "/kittop", "/kittop/register", "/kittop/checkEmail", "/kittop/checkNickName",
                        "/kittop/item/**", "/kittop/images/**", "/kittop/emailConfirm", "/kittop/emailVerify",
                        "/kittop/getServerCode", "/kittop/loadPage/**").permitAll()
                //그 외 모든 경로는 모두 인증(로그인) 해야 접근 가능 설정
                .anyRequest().authenticated();

        /**
         * OAuth2 로그인 관련 설정
         */
        http.oauth2Login()
                //기존의 로그인 페이지 경로
                .loginPage("/kittop/login")
                .userInfoEndpoint()
                .userService(customOauth2UserService);

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/kittop/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll();

        /**
         * 자동 로그인  기능 스프링 시큐리티 자체적으로
         * rememberMe 기능을 지원하며
         * 별도의 uuid를 사용자별로 DB에 UUID를 저장하지 않아도
         * key 값을 통해 애플리케이션 전체 사용자가 인증을 할 때
         * 자동으로 토큰값을 생성해주며, 해당 토큰 값을 쿠키에 저장해줌과 동시에
         * 사용자가 인증을 할 때 스프링 시큐리티가 해당 토큰 값과 인증 사용자 일치여부를
         * 체크 해주는 설정
         */
//        http.rememberMe()
//                //로그인 폼에서 remember-me 이름의 파라미터 받아옴
//                .rememberMeParameter("remember-me")
//                //자동 로그인 토큰 유효기간 설정
//                .tokenValiditySeconds(60 * 60 * 24 * 7)
//                //토큰 생성에 사용되는 키
//                .key(rememberMeKey)
//                //사용자 정보를 로드하는 서비스
//                .userDetailsService(userDetailsService());
    }

    /**
     * CustomUserDetailsService 클래스에서 정의한
     * loadUserByUsername() 메소드를 사용하여
     * 사용자 인증 정보를 검색
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder);
    }

    /**
     * 스프링 시큐리티의 설정을 무시하는 경로
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/images/**", "/error");
    }
}
