package com.gym.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dto.ResponseErrorBodyDTO;
import com.gym.dto.trainer.TrainerUpdateDTO;
import com.gym.utils.JsonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class UsernameFilter extends OncePerRequestFilter {

    private final List<String> endpoints = List.of("trainers", "trainees", "users");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.matches("/login");
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String username = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) auth.getPrincipal()).getUsername();
        }

        if (isFilterEndpoint(path)) {
            boolean hasUsername = matchPath(path, username);
            if (!hasUsername) {
                setErrorResponse(response, username);
                return;
            }
        }
//        if (path.matches("/trainers")) {
//            if (request.getMethod().equalsIgnoreCase("PUT")) {
//                ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
////                String content = wrapper.getContentAsString();
//                TrainerUpdateDTO trainerUpdateDTO = JsonUtils.parseJsonString(wrapper.getContentAsString(),
//                        new TypeReference<>() {
//                        });
//                if (trainerUpdateDTO != null && username.equalsIgnoreCase(trainerUpdateDTO.getUserName())) {
//                    hasUsernameInBody = true;
//                }
//                if (!hasUsernameInBody) {
//                    setErrorResponse(response, username);
//                    return;
//                }
//            }
//        }

        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response, String username) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ResponseErrorBodyDTO errorBodyDTO = ResponseErrorBodyDTO
                .builder()
                .errors(List.of(String.format("Access for user %s denied", username)))
                .build();
        response.getWriter().write(JsonUtils.convertObjectToJson(errorBodyDTO));
    }

    private boolean matchPath(String path, String username) {
        for (String endpoint : endpoints) {
            if (path.matches("/" + endpoint + "/" + username + "(?:/.*)?" )) {
                return true;
            }
        }
        return false;
    }

    private boolean isFilterEndpoint(String path) {
        for (String endpoint : endpoints) {
            if (path.matches("/" + endpoint + "/.*")) {
                return true;
            }
        }
        return false;
    }
}
