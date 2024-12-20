package com.example.tobi.tokenproject.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RefreshTokenResponseDTO {
    private boolean validated;
    private String refreshToken;
    private String accessToken;
}