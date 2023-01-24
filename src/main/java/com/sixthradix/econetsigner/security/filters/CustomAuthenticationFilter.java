package com.sixthradix.econetsigner.security.filters;

import com.sixthradix.econetsigner.security.authentication.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationFilter implements Filter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            var http = (HttpServletRequest) request;

            //allow preflighted requests
            if(HttpMethod.OPTIONS.name().equals(((HttpServletRequest) request).getMethod())){
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(200);
            }else {
                String token = http.getHeader("Authorization");

                var authenticatedUser = new AuthenticatedUser(null);
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
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication not provided");
        }
    }

}
