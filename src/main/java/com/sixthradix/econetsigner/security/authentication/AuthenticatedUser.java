package com.sixthradix.econetsigner.security.authentication;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Getter
@Setter
public class AuthenticatedUser extends AbstractAuthenticationToken {

    private Long id;
    private String email;
    private String userName;
    private String fullName;
    private String role;
    private String token;

    public AuthenticatedUser(String userName, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userName = userName;
    }

    public AuthenticatedUser(String userName, String email, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userName = userName;
        this.email = email;
    }

    public AuthenticatedUser(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }



    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public String getPrincipal() {
        return userName;
    }
}
