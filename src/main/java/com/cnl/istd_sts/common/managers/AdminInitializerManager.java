package com.cnl.istd_sts.common.managers;

import com.cnl.istd_sts.common.enums.UserRole;
import com.cnl.istd_sts.features.users.UsersRepository;
import com.cnl.istd_sts.features.users.domain.UserEntity;
import com.cnl.istd_sts.features.users.domain.UserPersonalDataEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializerManager implements CommandLineRunner {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public AdminInitializerManager(
            UsersRepository usersRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.actuator.admin-name}") String adminEmail,
            @Value("${app.actuator.admin-password}") String adminPassword
    ) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        // Перевіряємо, чи існує адмін в базі
        if (!usersRepository.existsByEmail(adminEmail)) {
            System.out.println("⚡️ СТВОРЮЄМО СИСТЕМНОГО АДМІНІСТРАТОРА В БД...");

            UserEntity admin = new UserEntity();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(UserRole.ADMIN);
            admin.setPhoneNumber("0000000000");
            admin.setPersonalData(new UserPersonalDataEntity("Main Inherited Admin for Spring Boot", "Server", "XXXXXXX"));

            usersRepository.save(admin);
            System.out.println("✅ АДМІН СТВОРЕНИЙ: " + adminEmail);
        } else {
            System.out.println("ℹ️ Адмін вже існує в базі.");
        }
    }
}