package com.gym.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final LoginAttemptService attemptService;

    public AuthenticationFailureListener(LoginAttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String userName = event.getAuthentication().getName();
        attemptService.loginFailed(userName);
    }
}
