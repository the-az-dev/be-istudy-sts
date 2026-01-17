package com.cnl.istd_sts.features.users;

import com.cnl.istd_sts.features.users.domain.UserEntity;
import com.cnl.istd_sts.features.users.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping(value="/get/personal-data")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        return ResponseEntity.ok(usersService.getUserByMail(user.getEmail()));
    }

    @GetMapping(value="/get/all/byEmail", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAuthority(T(com.cnl.istd_sts.common.enums.UserRole).ADMIN.getAuthority())")
    public ResponseEntity<?> getUsersByEmail(@AuthenticationPrincipal UserEntity user, @RequestParam String email) {
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        if (email == null || email.isEmpty() || !(email instanceof String)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid email address");

        return ResponseEntity.ok(usersService.getUserByMail(email));
    }
}
