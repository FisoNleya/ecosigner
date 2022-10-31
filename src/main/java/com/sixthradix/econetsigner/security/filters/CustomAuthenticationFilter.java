package com.sixthradix.econetsigner.security.filters;

import com.sixthradix.econetsigner.security.authentication.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CustomAuthenticationFilter implements Filter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        var http = (HttpServletRequest) request;
        String token = http.getHeader("Authorization");

        var authenticatedUser = new AuthenticatedUser(extractAuthorities("Admin User"));
        authenticatedUser.setToken(token);
        Authentication result = authenticationManager.authenticate(authenticatedUser);
        result.setAuthenticated(true);
        if(result.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(result);
            filterChain.doFilter(request, response);
        }else {
            log.error("User not authenticated");
        }
    }

    private List<GrantedAuthority> extractAuthorities(String scopes){
        String[] data = scopes.split(" ");
        //remove roles
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String item: data){
            GrantedAuthority authority = new SimpleGrantedAuthority(item);
            authorities.add(authority);
        }
        return authorities;
    }
}
