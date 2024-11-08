package com.gym.config;

import com.gym.security.CustomAuthenticationFailureHandler;
import com.gym.security.UsernameFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UsernameFilter usernameFilter;

    public WebSecurityConfig(UsernameFilter usernamePathFilter) {
        this.usernameFilter = usernamePathFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(antMatcher(HttpMethod.POST, "/**"))
                        .ignoringRequestMatchers(antMatcher(HttpMethod.PUT, "/**"))
                        .ignoringRequestMatchers(antMatcher(HttpMethod.PATCH, "/**")))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/trainees/create").permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/trainers")).permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .failureHandler(new CustomAuthenticationFailureHandler()))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
