package com.sixthradix.econetsigner.models;


import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class User {

    private String username;
    private String email;
    private String fullName;
    private String role;

}
