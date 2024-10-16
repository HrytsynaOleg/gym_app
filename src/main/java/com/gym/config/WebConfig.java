package com.gym.config;

import com.gym.interceptor.RestLoggingInterceptor;
import com.gym.interceptor.RestTransactionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    RestTransactionInterceptor transactionInterceptor;

    @Autowired
    RestLoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(transactionInterceptor);
        registry.addInterceptor(loggingInterceptor);
    }
}
