package com.sixthradix.econetsigner.dtos;

import lombok.Data;

@Data
public class ValidationResponse {
    private int statusCode;
    private boolean success;
    private String message;
    private boolean valid;
    private String username;
    private String email;
    private String fullname;
    private String role;
}
