package com.cnl.istd_sts.features.auth;

import com.cnl.istd_sts.features.auth.dto.AuthResponse;
import com.cnl.istd_sts.features.auth.dto.SignInRequest;
import com.cnl.istd_sts.features.auth.dto.SignUpRequest;
import com.cnl.istd_sts.features.users.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AuthResponse> login(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping(value = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AuthResponse> register(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @GetMapping(value = "/user", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UserEntity> testFind(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.getUserByMail(request.getEmail()));
    }
}
