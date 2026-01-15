package com.cnl.istd_sts.features.users;

import com.cnl.istd_sts.features.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneByEmail(String email);

    boolean existsByEmail(String email);

}
