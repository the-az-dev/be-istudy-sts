package com.cnl.istd_sts.features.users;

import com.cnl.istd_sts.features.users.domain.UserEntity;
import com.cnl.istd_sts.features.users.dto.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public UserEntity getUserByMail(String email) {
        UserEntity user = usersRepository.findOneByEmail(email).orElseThrow();
        return user;
    }
}
