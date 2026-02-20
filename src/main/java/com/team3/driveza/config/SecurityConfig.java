package com.team3.driveza.config;

import com.team3.driveza.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setUserDetailsPasswordService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/error")
                    .permitAll()
                    .requestMatchers("/login", "/register", "/oauth2/**", "/login/oauth2/**")
                    .permitAll()
                    .requestMatchers("/", "/login", "/register", "/cars", "/cars/**", "/car-details", "/car-details/**", "/403")
                    .permitAll()
                    .requestMatchers("/oauth2/**", "/login/oauth2/**")
                    .permitAll()

                    .requestMatchers("/cars", "/cars/**", "/map", "/map/**", "/account", "/account/**",
                            "/car-details/**", "/rentals/**")
                    .hasAnyRole("USER", "ADMIN")

                    .requestMatchers("/admin/**", "/vehicles/**", "/models/**", "/users/**")
                    .hasRole("ADMIN")

                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(authenticationSuccessHandler())
                    .failureUrl("/login?error")
                    .permitAll()
            )
            .rememberMe(remember -> remember
                    .rememberMeParameter("remember-me")
                    .tokenValiditySeconds(24 * 60 * 60)
                    .key("driveza-rmb-key")
            )
            .exceptionHandling(exception -> exception.accessDeniedPage("/403"))
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .defaultSuccessUrl("/cars", true)
                    .failureUrl("/login?error")
            );

    return http.build();
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return (request, response, authentication) -> {
      boolean isAdmin = authentication.getAuthorities()
              .stream()
              .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));
      response.sendRedirect(isAdmin ? "/admin/dashboard" : "/cars");
    };
  }
}
