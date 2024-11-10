package com.gym.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Bad credentials";
        if (exception.getMessage().equalsIgnoreCase("blocked")) {
            errorMessage = "User blocked";
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(errorMessage);
        response.sendRedirect("/login?error=" + errorMessage);
//        if (exception.getMessage().equalsIgnoreCase("blocked")) {
//            response.sendRedirect("/login?error=disabled");
//        } else if (exception.getMessage().equalsIgnoreCase("Bad credentials")) {
//            response.sendRedirect("/login?error=invalid");
//        } else {
//            response.sendRedirect("/login?error=true");
//        }
    }
}
