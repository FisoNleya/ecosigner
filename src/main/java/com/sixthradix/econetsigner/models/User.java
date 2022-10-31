package com.sixthradix.econetsigner.models;


import lombok.Data;

import java.util.ArrayList;

@Data
public class User {

    private String user_name;
    private ArrayList<String> scope;
    private String fullName;
    private int exp;
    private ArrayList<String> authorities;
    private String jti;
    private String email;
    private String client_id;
}
