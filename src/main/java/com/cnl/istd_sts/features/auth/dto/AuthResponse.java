package com.cnl.istd_sts.features.auth.dto;

import com.cnl.istd_sts.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AuthResponse {
    public String token;
    public UserRole role;
}
