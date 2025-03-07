package com.springtest.Demo.auth.dto;

import com.springtest.Demo.repository.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionDTO implements Serializable {
    private Long id;
    private String username;
    private String email;
    private Role role;
} 