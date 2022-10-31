package com.sixthradix.econetsigner.security.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixthradix.econetsigner.models.User;
import com.sixthradix.econetsigner.security.authentication.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Value("${app.tokenValidationUrl}")
    private String tokenValidationUrl;

    private final ObjectMapper objectMapper;

    public CustomAuthenticationProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();

        User user = retrieveUser(token);
        if (user != null) {
            var authenticatedUser = new AuthenticatedUser(user.getUser_name(), extractAuthorities(user.getAuthorities()));
            authenticatedUser.setToken(token);
            authenticatedUser.setUserName(user.getUser_name());
            authenticatedUser.setEmail(user.getEmail());

            return authenticatedUser;
        } else {
            return null;
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AuthenticatedUser.class);
    }

    private User retrieveUser(String token){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(tokenValidationUrl))
                    .header("Authorization", token)
                    .version(HttpClient.Version.HTTP_2)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), User.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private List<GrantedAuthority> extractAuthorities(List<String> scopes){
        //remove roles
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String item: scopes){
            GrantedAuthority authority = new SimpleGrantedAuthority(item);
            authorities.add(authority);
        }
        return authorities;
    }
}
