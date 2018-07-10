package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.*;
import org.uatransport.entity.dto.ForgetPasswordDTO;
import org.uatransport.entity.dto.LoginDTO;
import org.uatransport.entity.dto.UpdateUserRoleDTO;
import org.uatransport.entity.dto.UserDTO;
import org.uatransport.exception.EmailSendException;
import org.uatransport.service.TemporaryDataConfirmationService;
import org.uatransport.service.UserService;
import org.uatransport.service.email.EmailService;
import org.uatransport.service.implementation.ExpirationCheckService;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;
    @Autowired
    private TemporaryDataConfirmationService temporaryDataConfirmationService;


    @Autowired
    private EmailService emailService;

    @Autowired
    private ExpirationCheckService expirationCheckService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody UserDTO userDTO) {

        userService.signup(userDTO);
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        final String confirmUrl = "http://localhost:4200/user" + "/activate/" + uuid;
        String email = userDTO.getEmail();
        String firstName = userDTO.getFirstName();
        temporaryDataConfirmationService
            .save(temporaryDataConfirmationService.makeRegistrationConfirmationEntity(uuid, email));
        ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
        try {
            emailExecutor.execute(() -> {
                emailService.prepareAndSendConfirmRegistrationEmail(email, firstName, confirmUrl);
            });
        } catch (MailException e) {
            throw  new EmailSendException("Could not send email to"+email, HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            emailExecutor.shutdown();
        }
        return ResponseEntity.status(HttpStatus.OK).body("Please check your email, and confirm registration");
    }
    @PostMapping("/activate/{uuidFromUrl}")
    public ResponseEntity activateUser(@PathVariable String uuidFromUrl) {
        Optional<TemporaryDataConfirmation> checkedTemporaryDataConfirmation = expirationCheckService
            .getTemporaryDataConfirmationWithExpirationChecking(uuidFromUrl);

        if (checkedTemporaryDataConfirmation.isPresent()) {
            User user = userService.getUserByEmail(checkedTemporaryDataConfirmation.get().getUserEmail());

            if ((uuidFromUrl.equals(checkedTemporaryDataConfirmation.get().getUuid()))
                && (checkedTemporaryDataConfirmation.get()
                .getConfirmationType() == ConfirmationType.REGISTRATION_CONFIRM)) {

                userService.activateUserByEmail(checkedTemporaryDataConfirmation.get().getUserEmail());
                temporaryDataConfirmationService.delete(checkedTemporaryDataConfirmation.get());

                ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
                try {
                    emailExecutor.execute(() -> {
                        emailService.prepareAndSendWelcomeEmail(user.getEmail(), user.getFirstName());
                    });
                } catch (MailException e) {
                    throw  new EmailSendException("Could not send email to "+user.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
                } finally {
                    emailExecutor.shutdown();
                }
                return new ResponseEntity<>("Your account has been successfully activated", HttpStatus.OK);
            }
        } else {

            return new ResponseEntity<>("Error during account activation", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Error during account activation", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {

        String token = userService.signin(loginDTO);
        response.setHeader("Authorization", token);

        return ResponseEntity.ok(new TokenModel(token));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {

        userService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Integer id) {
        User updatedUser = userService.update(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/me")
    public User getCurrentUser(Principal principal) {
        return userService.getUser(principal);
    }

    @PostMapping(value = "/update/password/{uuidFromUrl}")
    public ResponseEntity saveUserPassword(@PathVariable String uuidFromUrl) {
        Optional<TemporaryDataConfirmation> checkedTemporaryDataConfirmation =
            expirationCheckService.getTemporaryDataConfirmationWithExpirationChecking(uuidFromUrl);
        if (checkedTemporaryDataConfirmation.isPresent()) {
            if ((uuidFromUrl.equals(checkedTemporaryDataConfirmation.get().getUuid()))
                && (checkedTemporaryDataConfirmation.get()
                .getConfirmationType() == ConfirmationType.PASSWORD_CONFIRM)) {
                String newPassword = checkedTemporaryDataConfirmation.get().getNewPassword();
                String userEmail = checkedTemporaryDataConfirmation.get().getUserEmail();

                userService.updateUserEncodedPassword(newPassword, userEmail);
                temporaryDataConfirmationService.delete(checkedTemporaryDataConfirmation.get());

                return new  ResponseEntity<>("You are successfully updated password",HttpStatus.OK);
            }
        } else {
            return new  ResponseEntity<>("Error during password changing",HttpStatus.BAD_REQUEST);
        }

        return new  ResponseEntity<>("Error during password changing",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/update/password/confirm")
    public ResponseEntity sendConfirmation(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        final String confirmUrl = "http://localhost:4200/user" + "/update/password/" + uuid;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String firstName = userService.getUserByEmail(email).getFirstName();
        temporaryDataConfirmationService.save(
            temporaryDataConfirmationService
                .makePasswordConfirmationEntity(uuid, forgetPasswordDTO.getNewPassword(), email));
        sendPasswordChangeConfirmationEmail(email, firstName, confirmUrl);

        return new  ResponseEntity<>("Please, check your email",HttpStatus.OK);
    }

    @PostMapping("/forget/password/confirm")
    public ResponseEntity forgetPasswordSendConfirmation(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        final String confirmUrl = "http://localhost:4200/user" + "/update/password/" + uuid;
        String userEmail = forgetPasswordDTO.getEmail();
        String firstName = userService.getUserByEmail(userEmail).getFirstName();
        temporaryDataConfirmationService.save(
            temporaryDataConfirmationService
                .makePasswordConfirmationEntity(uuid, forgetPasswordDTO.getNewPassword(), userEmail));
        sendPasswordChangeConfirmationEmail(userEmail, firstName, confirmUrl);

        return new  ResponseEntity<>("Please, check your email",HttpStatus.OK);
    }

    @PutMapping("/update-role")
    public ResponseEntity updateUserRole(@RequestBody UpdateUserRoleDTO updateUserRoleDTO) {
        String role = updateUserRoleDTO.getRole();
        String email = updateUserRoleDTO.getEmail();
        return new ResponseEntity<>(userService.updateUserRole(role,email), HttpStatus.OK);
    }

    private void sendPasswordChangeConfirmationEmail(String userEmail, String firstName, String confirmUrl) {
        ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
        try {
            emailExecutor.execute(() -> {
                emailService.prepareAndSendConfirmPassEmail(userEmail, firstName, confirmUrl);
            });
        }catch (MailException e) {
            throw  new EmailSendException("Could not send email to"+userEmail, HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            emailExecutor.shutdown();
        }
    }
}


