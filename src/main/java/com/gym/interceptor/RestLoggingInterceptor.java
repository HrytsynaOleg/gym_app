package com.gym.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Log4j2
public class RestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        String endpoint = request.getRequestURI();
        log.info("Start API transaction Id: {}, Method: {}, Endpoint: {}",
                MDC.get("transactionId"), method, endpoint);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        int status = response.getStatus();
        String statusDescription = HttpStatus.valueOf(status).getReasonPhrase();
        String method = request.getMethod();
        String endpoint = request.getRequestURI();
        log.info("End API transaction Id: {}, Method: {}, Endpoint: {}, Status: {} {}",
                MDC.get("transactionId"), method, endpoint, status, statusDescription);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
