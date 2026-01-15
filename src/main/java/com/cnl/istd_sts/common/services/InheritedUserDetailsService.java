package com.cnl.istd_sts.common.services;

import com.cnl.istd_sts.features.users.UsersRepository;
import com.cnl.istd_sts.features.users.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class InheritedUserDetailsService implements UserDetailsService {

    @Autowired
    private final UsersRepository usersRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = usersRepository.findOneByEmail(email).orElseThrow();

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        var authority = new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name());

        // 3. Повертаємо системний об'єкт User
        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(), // Хешований пароль
                Collections.singletonList(authority)
        );
    }
}
