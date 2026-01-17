package com.cnl.istd_sts.config;

import com.cnl.istd_sts.common.entities.InheritedUserDetails;
import com.cnl.istd_sts.common.filters.JwtAuthenticationFilter;
import com.cnl.istd_sts.common.services.InheritedUserDetailsService;
import com.cnl.istd_sts.features.users.UsersRepository;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UsersRepository usersRepository;

    @Value("${app.core.api-version}")
    private String apiVersion;

    private final AdminServerProperties adminServer;
    private final String adminName;
    private final String adminPass;
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(
            AdminServerProperties adminServer,
            @Value("${app.actuator.admin-name}") String adminName,
            @Value("${app.actuator.admin-password}") String adminPass, JwtAuthenticationFilter jwtAuthFilter
    ) throws Exception {
        this.adminServer = adminServer;
        this.adminName = adminName;
        this.adminPass = adminPass;
        this.jwtAuthFilter = jwtAuthFilter;

    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usersRepository.findOneByEmail(username)
                .map(InheritedUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // ========================================================================
    // STEP 1: API (JWT, Stateless)
    // ========================================================================
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/" + apiVersion + "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // ========================================================================
    // STEP 2: Actuator & Admin Server (Form Login, Stateful)
    // ========================================================================
    @Bean
    @Order(2)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.getContextPath() + "/");

        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(this.adminServer.getContextPath() + "/assets/**").permitAll()
                        .requestMatchers(this.adminServer.getContextPath() + "/login").permitAll()
                        .requestMatchers(this.adminServer.getContextPath() + "/logout").permitAll()
                        .requestMatchers("/actuator/**", "/instances").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(this.adminServer.getContextPath() + "/login")
                        .successHandler(successHandler)
                )
                .logout(logout -> logout
                        .logoutUrl(this.adminServer.getContextPath() + "/logout")
                        .logoutSuccessUrl(this.adminServer.getContextPath() + "/login?logout")
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                this.adminServer.getContextPath() + "/instances",
                                this.adminServer.getContextPath() + "/actuator/**",
                                this.adminServer.getContextPath() + "/logout"
                        )
                )
                .build();
    }

    // ========================================================================
    // BEANS
    // ========================================================================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ⚠️ ВАЖЛИВО: Це менеджер для логіну ЮЗЕРІВ через API
    // Використовуй @Primary, якщо є конфлікти, або @Qualifier у сервісах
    @Bean
    @Primary
    public AuthenticationManager authenticationManager(InheritedUserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // --- JWT ---

    @Bean("adminUserDetailsService")
    public UserDetailsService adminUserDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username(adminName)
                .password(adminPass)
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
