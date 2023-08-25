package com.kittop.filter;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.domain.Role;
import com.kittop.service.CartService;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Component
@WebFilter(urlPatterns = {"/kittop/*"})
@Log4j2
public class PayFilter implements Filter {

    @Autowired
    private CartService cartService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(uri.contains("/checkNickName")){
            chain.doFilter(request, response);
            return;
        }

        if(authentication != null && !authentication.getName().equals("anonymousUser")
                && ((CustomUserDetails)authentication.getPrincipal()).getUserVO().getRole().equals(Role.ROLE_GOOGLE)){
            if(!uri.contains("/user/modify")){
                resp.sendRedirect("/kittop/user/modify");
                return;
            }
        }

//        if(authentication != null && userDetails.getUserVO().getRole().equals(Role.ROLE_GOOGLE)){
//            chain.doFilter(request, response);
//            resp.sendRedirect("/kittop/user/modify");
//            return;
//        }


        if (authentication != null && !authentication.getName().equals("anonymousUser")) {
            log.info("name : " + authentication.getName());
            String email = authentication.getName();
            if(cartService != null){
                req.getSession().setAttribute("cartCount", cartService.findCartCountByUserEmail(email));
            }

        }

        log.info("URI : " + uri);
        if(uri.contains("order/pay") || uri.contains("kittop/payCart") || uri.contains("js") || uri.contains("css") || uri.contains("images") || uri.contains("cart/list") || uri.contains("toss")){
            chain.doFilter(request, response);
            return;
        }
        log.info("SessionRemove : " + uri);

        req.getSession().removeAttribute("cart");
        chain.doFilter(request, response);
    }
}
