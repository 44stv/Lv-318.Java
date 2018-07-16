package org.uatransport.service;

import org.springframework.stereotype.Component;
import org.uatransport.entity.dto.LoginDTO;
import org.uatransport.entity.dto.UserDTO;
import org.uatransport.exception.UserValidationException;

@Component
public interface UserValidatorService {
    void validateEmailOnRegistration(String email) throws UserValidationException;

    void validateEmailOnLogin(String email) throws UserValidationException;

    void validateFirstname(String firstName) throws UserValidationException;

    void validateLastname(String lastName) throws UserValidationException;

    void validatePassword(String password) throws UserValidationException;

    void checkPasswords(String password, String confirmPassword) throws UserValidationException;

    void validateUserOnRegistration(UserDTO userDTO) throws UserValidationException;

    void validateUserOnLogin(LoginDTO loginDTO) throws UserValidationException;

    // TODO
    boolean validateForUnactivating(String email);

    void validateForActivating(String email) throws UserValidationException;
}
