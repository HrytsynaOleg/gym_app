package com.gym.service;

import jakarta.validation.ValidationException;

public interface IModelValidator {
    <T> void  validate(T model) throws ValidationException;
}
