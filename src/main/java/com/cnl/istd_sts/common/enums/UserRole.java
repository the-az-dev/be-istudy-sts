package com.cnl.istd_sts.common.enums;

public enum UserRole {
    TUTOR,
    ADMIN,
    STUDENT,
    GUEST;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
