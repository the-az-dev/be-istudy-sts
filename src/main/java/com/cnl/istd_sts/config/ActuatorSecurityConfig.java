package com.cnl.istd_sts.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class ActuatorSecurityConfig {

    private final AdminServerProperties adminServer;
    private final String adminName;
    private final String adminPass;

    public ActuatorSecurityConfig(
            AdminServerProperties adminServer,
            @Value("${app.actuator.admin-name}") String adminName,
            @Value("${app.actuator.admin-password}") String adminPass
    ) {
        this.adminServer = adminServer;
        this.adminName = adminName;
        this.adminPass = adminPass;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.getContextPath() + "/");

        http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(this.adminServer.getContextPath() + "/assets/**").permitAll();
                    auth.requestMatchers(this.adminServer.getContextPath() + "/login").permitAll();

                    auth.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN");

                    auth.anyRequest().permitAll();
                })
                .formLogin(form -> {
                    form.loginPage(this.adminServer.getContextPath() + "/login");
                    form.successHandler(successHandler);
                })
                .logout(logout -> {
                    logout.logoutUrl(this.adminServer.getContextPath() + "/logout");
                })
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> {
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

                    csrf.ignoringRequestMatchers(
                            this.adminServer.getContextPath() + "/instances",
                            this.adminServer.getContextPath() + "/actuator/**"
                    );
                });

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username(adminName)
                .password(adminPass)
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}