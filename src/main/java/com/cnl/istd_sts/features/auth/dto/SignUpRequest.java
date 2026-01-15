package com.cnl.istd_sts.features.auth.dto;

import com.cnl.istd_sts.common.enums.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class SignUpRequest {

    public String email;
    public String password;
    public UserRole role;

}
