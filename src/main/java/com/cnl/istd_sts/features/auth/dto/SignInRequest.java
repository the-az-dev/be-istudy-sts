package com.cnl.istd_sts.features.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {
    public String email;
    public String password;
}
