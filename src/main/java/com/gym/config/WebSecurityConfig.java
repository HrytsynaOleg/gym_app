package com.gym.config;

import com.gym.security.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/*/create"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/trainers/create", "/trainees/create").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .failureHandler(new CustomAuthenticationFailureHandler()))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


}
