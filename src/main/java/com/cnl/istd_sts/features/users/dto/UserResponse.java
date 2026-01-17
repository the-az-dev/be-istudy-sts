package com.cnl.istd_sts.features.users.dto;

import lombok.*;

@Data
@Builder
public class UserResponse {
    public String id;
    public String email;
    public String fullName;
    public String phoneNumber;
    public String role;
    public String city;
}
