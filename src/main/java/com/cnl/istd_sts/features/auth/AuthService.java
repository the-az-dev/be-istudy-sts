package com.cnl.istd_sts.features.auth;

import com.cnl.istd_sts.common.exceptions.CredentialsNotValid;
import com.cnl.istd_sts.common.exceptions.UserExistsExceptions;
import com.cnl.istd_sts.common.services.TokenManagmentService;
import com.cnl.istd_sts.features.auth.dto.AuthResponse;
import com.cnl.istd_sts.features.auth.dto.SignInRequest;
import com.cnl.istd_sts.features.auth.dto.SignUpRequest;
import com.cnl.istd_sts.features.users.UsersRepository;
import com.cnl.istd_sts.features.users.domain.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManagmentService tokenManagmentService;

    public AuthResponse signIn(SignInRequest signInRequest) {
        if (!usersRepository.existsByEmail(signInRequest.getEmail())) {
            throw new CredentialsNotValid();
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword())
        );
        return new AuthResponse(
                tokenManagmentService.generateToken(authentication),
                usersRepository.findOneByEmail(signInRequest.getEmail()).orElseThrow().getRole()
        );
    }

    public AuthResponse signUp(SignUpRequest signUpRequest){
        if (usersRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserExistsExceptions(signUpRequest.getEmail());
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        newUser.setRole(signUpRequest.getRole());

        usersRepository.save(newUser);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword())
        );

        return new AuthResponse(
                tokenManagmentService.generateToken(authentication),
                signUpRequest.getRole()
        );
    }
}
