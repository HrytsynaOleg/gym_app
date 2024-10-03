package com.gym.service.impl;

import com.gym.service.IModelValidator;
import jakarta.validation.ConstraintViolation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ValidationException;
import java.util.Set;

@Service
@Log4j2
public class ModelValidator implements IModelValidator {

    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    @Autowired
    public ModelValidator(LocalValidatorFactoryBean localValidatorFactoryBean) {
        this.localValidatorFactoryBean = localValidatorFactoryBean;
    }

    @Override
    public <T> void validate(T model) throws ValidationException {
        Set<ConstraintViolation<T>> validate = localValidatorFactoryBean.getValidator().validate(model);
        if (!validate.isEmpty()) {
            log.error("Model validation error");
            throw new ValidationException("Validation failed");
        }
    }
}
