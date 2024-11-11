package com.gym.security;

import com.gym.dto.ResponseErrorBodyDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserModel;
import com.gym.service.impl.UserService;
import com.gym.utils.JsonUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService tokenService;
    private final UserService userService;
    private final List<String> endpoints = List.of("trainers", "trainees", "training");

    public JwtAuthenticationFilter(JwtTokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String headerName = "Authorization";
        String header = request.getHeader(headerName);
        String bearerPrefix = "Bearer ";
        String tokenUserName;
        if (matchPath(path)) {
            if (!header.startsWith(bearerPrefix)) {
                setErrorResponse(response);
                return;
            }
            try {
                tokenUserName = tokenService.getUserName(header.substring(bearerPrefix.length()));
            } catch (JwtException e) {
                setErrorResponse(response);
                return;
            }
            UserModel userModel;
            try {
                userModel = userService.getUserProfile(tokenUserName);
            } catch (IncorrectCredentialException e) {
                setErrorResponse(response);
                return;
            }
            if (!userModel.getToken().equals(header.substring(bearerPrefix.length()))) {
                setErrorResponse(response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean matchPath(String path) {
        for (String endpoint : endpoints) {
            if (path.matches("/" + endpoint)) {
                return true;
            }
        }
        return false;
    }

    private void setErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseErrorBodyDTO errorBodyDTO = ResponseErrorBodyDTO
                .builder()
                .errors(List.of("Access denied"))
                .build();
        response.getWriter().write(JsonUtils.convertObjectToJson(errorBodyDTO));
    }
}
