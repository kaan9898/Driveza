package com.team3.driveza.config;
import com.team3.driveza.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserService userService;
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, UserDetailsPasswordService userDetailsPasswordService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setUserDetailsPasswordService(userDetailsPasswordService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService, UserDetailsPasswordService userDetailsPasswordService) throws Exception {

    http
            .authenticationProvider(authenticationProvider(userDetailsService, userDetailsPasswordService))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/error")
                    .permitAll()
                    .requestMatchers("/login", "/register", "/cars", "/cars/**", "/car-details", "/car-details/**")
                    .permitAll()
                    .requestMatchers("/oauth2/**", "/login/oauth2/**")
                    .permitAll()
                    .requestMatchers("/admin/**", "/vehicles/**", "/models/**", "/users/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/account", "/account/**", "/rentals/**")
                    .hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("username")
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