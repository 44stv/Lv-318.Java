package org.uatransport.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.uatransport.entity.Role;
import org.uatransport.entity.User;
import org.uatransport.entity.dto.LoginDTO;
import org.uatransport.entity.dto.UserDTO;
import org.uatransport.exception.UserValidationException;
import org.uatransport.repository.UserRepository;
import org.uatransport.service.UserValidatorService;

@Component
@RequiredArgsConstructor
public class UserValidatorServiceImp implements UserValidatorService {

    private final UserRepository userRepository;

    @Value("${validation.email.min}")
    private int emailMin;
    @Value("${validation.email.max}")
    private int emailMax;

    @Value("${validation.firstname.min}")
    private int firstnameMin;
    @Value("${validation.firstname.max}")
    private int firstnameMax;

    @Value("${validation.lastname.min}")
    private int lastnameMin;
    @Value("${validation.lastname.max}")
    private int lastnameMax;

    @Value("${validation.password.min}")
    private int passwordMin;
    @Value("${validation.password.max}")
    private int passwordMax;

    @Override
    public void validateEmailOnRegistration(String email) throws UserValidationException {
        if (email == null || email.length() < emailMin || email.length() > emailMax) {

            throw new UserValidationException("Invalid email");
        }
        User user = userRepository.findByEmail(email);
        if (user != null && user.getRole() == Role.USER) {
            throw new UserValidationException("User with this e-mail already exists");
        }
    }

    @Override
    public void validateEmailOnLogin(String email) throws UserValidationException {
        if (email == null || email.length() < emailMin || email.length() > emailMax) {

            throw new UserValidationException("Entered e-mail adress is not valid");
        }
        if (userRepository.findByEmail(email) == null) {
            throw new UserValidationException("User with this e-mail doesn`t exist");
        }
    }

    @Override
    public void validateFirstname(String firstname) throws UserValidationException {
        if (firstname == null || firstname.length() < firstnameMin || firstname.length() > firstnameMax) {

            throw new UserValidationException("Firstname is invalid");
        }
    }

    @Override
    public void validateLastname(String lastname) throws UserValidationException {
        if (lastname == null || lastname.length() < lastnameMin || lastname.length() > lastnameMax) {

            throw new UserValidationException("Lastname is invlaid");
        }
    }

    @Override
    public void validatePassword(String password) throws UserValidationException {
        if (password == null || password.length() < passwordMin || password.length() > passwordMax) {

            throw new UserValidationException("Password is invalid");
        }
    }

    @Override
    public void checkPasswords(String password, String passwordConfirmation) throws UserValidationException {
        if (!password.equals(passwordConfirmation)) {
            throw new UserValidationException("Passwords doesn`t match");
        }
    }

    @Override
    public void validateUserOnRegistration(UserDTO userDTO) throws UserValidationException {
        validateEmailOnRegistration(userDTO.getEmail());
        validateFirstname(userDTO.getFirstName());
        validateLastname(userDTO.getLastName());
        validatePassword(userDTO.getPassword());
        checkPasswords(userDTO.getPassword(), userDTO.getPasswordConfirmation());

    }

    @Override
    public void validateUserOnLogin(LoginDTO loginDTO) throws UserValidationException {
        validateEmailOnLogin(loginDTO.getEmail());
        validatePassword(loginDTO.getPassword());

    }

    @Override
    public boolean validateForUnactivating(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getRole() == Role.UNACTIVATED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void validateForActivating(String email) throws UserValidationException {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getRole() == Role.UNACTIVATED) {
            throw new UserValidationException("Account is not activated");
        }
    }
}
