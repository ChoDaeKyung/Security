package com.example.tobi.tokenproject.dto;

import com.example.tobi.tokenproject.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponseDTO {
    private Long id;
    private String userId;
    private String userName;
    private Role role;
}