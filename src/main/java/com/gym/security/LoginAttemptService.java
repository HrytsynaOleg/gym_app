package com.gym.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Service
public class LoginAttemptService {
    @Value("${attempt.max}")
    private int MAX_ATTEMPT;
    @Value("${attempt.block.duration}")
    private int BLOCKING_DURATION;

    private LoadingCache<String, Integer> attemptsCache;

    public void loginFailed(final String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (NullPointerException | ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
        long size = attemptsCache.size();
    }

    public void loginSuccess(final String key) {
        attemptsCache.put(key, 0);
    }

    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

    @PostConstruct
    public void init() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(BLOCKING_DURATION, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key)   {
                        return 0;
                    }
                });
    }
}
