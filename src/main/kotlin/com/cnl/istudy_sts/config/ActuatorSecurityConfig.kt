package com.cnl.istudy_sts.config
import de.codecentric.boot.admin.server.config.AdminServerProperties
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
@EnableWebSecurity
class ActuatorSecurityConfig (
    private val adminServer: AdminServerProperties,
    @Value("\${app.actuator.admin-name}") private val adminName: String,
    @Value("\${app.actuator.admin-password}") private val adminPass: String
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val successHandler = SavedRequestAwareAuthenticationSuccessHandler()
        successHandler.setTargetUrlParameter("redirectTo")
        successHandler.setDefaultTargetUrl(this.adminServer.contextPath.toString() + "/")
        http
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(this.adminServer.contextPath + "/assets/**").permitAll()
                auth.requestMatchers(this.adminServer.contextPath + "/login").permitAll()

                auth.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")

                auth.anyRequest().permitAll()
            }
            .formLogin { form ->
                form.loginPage(this.adminServer.contextPath + "/login")
                form.successHandler(successHandler)
            }
            .logout { logout ->
                logout.logoutUrl(this.adminServer.contextPath + "/logout")
            }
            .httpBasic(Customizer.withDefaults())
            .csrf {  csrf ->

                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                csrf.ignoringRequestMatchers(
                    this.adminServer.contextPath + "/instances",
                    this.adminServer.contextPath + "/actuator/**"
                )
            }

        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val admin = User.withDefaultPasswordEncoder()
            .username(adminName)
            .password(adminPass)
            .roles("ADMIN")
            .build()

        return InMemoryUserDetailsManager(admin)
    }
}