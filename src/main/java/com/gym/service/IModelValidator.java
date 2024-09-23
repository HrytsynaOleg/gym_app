package com.gym.service;

import javax.validation.ValidationException;

public interface IModelValidator {
    <T> void  validate(T model) throws ValidationException;
}
