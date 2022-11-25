
package com.sixthradix.econetsigner.dtos.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUserDto implements Serializable {

    private static final long serialVersionUID = 3424323345L;

    @JsonProperty("userName")
    private String userName;
    @JsonProperty("scope")
    private List<String> scope;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("exp")
    private Integer exp;
    @JsonProperty("authorities")
    private List<String> authorities;
    @JsonProperty("jti")
    private String jti;
    @JsonProperty("email")
    private Object email;
    @JsonProperty("client_id")
    private String clientId;


}
