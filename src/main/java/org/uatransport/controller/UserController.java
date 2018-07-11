package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.*;
import org.uatransport.entity.dto.*;
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

    @Value("${server.url}")
    private String serverUrl;

    @Value("localhost:4200/main")
    private String invitationLink;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody UserDTO userDTO) {
        if (userDTO.getPassword().equals(userDTO.getPasswordConfirmation())) {
            userService.signup(userDTO);
            final String uuid = UUID.randomUUID().toString().replace("-", "");
            final String confirmUrl = serverUrl + "/main/user/activate/" + uuid;
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
                throw new EmailSendException("Could not send email to " + email, HttpStatus.INTERNAL_SERVER_ERROR);

            } finally {
                emailExecutor.shutdown();
            }
            return ResponseEntity.status(HttpStatus.OK).body(new InfoResponse("Please check your email, and confirm registration"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InfoResponse("Password and passwordConfirm are not equals"));

        }
    }

    @PostMapping("/activate")
    public ResponseEntity activateUser(@RequestBody String uuidFromUrl) {
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
                    throw new EmailSendException("Could not send email to " + user.getEmail(),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                } finally {
                    emailExecutor.shutdown();
                }
                return new ResponseEntity<>(new InfoResponse("Your account has been successfully activated"),
                        HttpStatus.OK);
            }
        } else {

            return new ResponseEntity<>("Error during account activation", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Error during account activation", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        if (userService.getUserByEmail(loginDTO.getEmail()).getRole() != Role.UNACTIVATED) {
            String token = userService.signin(loginDTO);
            response.setHeader("Authorization", token);

            return ResponseEntity.ok(new TokenModel(token));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InfoResponse("Your account is not activated"));

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

    @PostMapping(value = "/update/password")
    public ResponseEntity saveUserPassword(@RequestBody String uuidFromUrl) {
        Optional<TemporaryDataConfirmation> checkedTemporaryDataConfirmation = expirationCheckService
                .getTemporaryDataConfirmationWithExpirationChecking(uuidFromUrl);
        if (checkedTemporaryDataConfirmation.isPresent()) {
            if ((uuidFromUrl.equals(checkedTemporaryDataConfirmation.get().getUuid()))
                    && (checkedTemporaryDataConfirmation.get()
                            .getConfirmationType() == ConfirmationType.PASSWORD_CONFIRM)) {
                String newPassword = checkedTemporaryDataConfirmation.get().getNewPassword();
                String userEmail = checkedTemporaryDataConfirmation.get().getUserEmail();

                userService.updateUserEncodedPassword(newPassword, userEmail);
                temporaryDataConfirmationService.delete(checkedTemporaryDataConfirmation.get());

                return new ResponseEntity<>(new InfoResponse("You are successfully updated password"), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("Error during password changing", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Error during password changing", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/forget/password/confirm")
    public ResponseEntity forgetPasswordSendConfirmation(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        if(forgetPasswordDTO.getPassword().equals(forgetPasswordDTO.getPasswordConfirmation())){
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        final String confirmUrl = serverUrl + "/main/user/forgetpass/" + uuid;
        String userEmail = forgetPasswordDTO.getEmail();
        String firstName = userService.getUserByEmail(userEmail).getFirstName();
        temporaryDataConfirmationService.save(
            temporaryDataConfirmationService
                .makePasswordConfirmationEntity(uuid, forgetPasswordDTO.getPassword(), userEmail));
        temporaryDataConfirmationService.save(temporaryDataConfirmationService.makePasswordConfirmationEntity(uuid,
                forgetPasswordDTO.getPassword(), userEmail));
            ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
            try {
                emailExecutor.execute(() -> {
                    emailService.prepareAndSendConfirmPassEmail(userEmail, firstName, confirmUrl);
                });
            } catch (MailException e) {
                throw new EmailSendException("Could not send email to " + userEmail, HttpStatus.INTERNAL_SERVER_ERROR);
            } finally {
                emailExecutor.shutdown();
            }

            return new ResponseEntity<>(new InfoResponse("Please, check your email "), HttpStatus.OK);
        }return new ResponseEntity<>(new InfoResponse("Password and PasswordConfirmation are not equals"), HttpStatus.BAD_REQUEST);

    }

    @PutMapping("/update-role")
    public ResponseEntity updateUserRole(@RequestBody UpdateUserRoleDTO updateUserRoleDTO) {
        String role = updateUserRoleDTO.getRole();
        String email = updateUserRoleDTO.getEmail();
        return new ResponseEntity<>(userService.updateUserRole(role, email), HttpStatus.OK);
    }


    @PostMapping("/social")
    public ResponseEntity socialSignIn(@RequestBody UserDTO userDTO) {

        if (userService.existUserByEmail(userDTO.getEmail())) {
            //
        } else {
            userService.signup(userDTO);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/invite")
    public ResponseEntity inviteFriend(@RequestBody FriendInvitationDTO friendInvitationDTO, Principal principal) {
        String recipient = friendInvitationDTO.getFriendEmail();
        String userName = userService.getUser(principal).getFirstName() + " " + userService.getUser(principal).getLastName();
        String friendName = friendInvitationDTO.getFriendName();

        ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
        try {
            emailExecutor.execute(() -> {
                emailService.prepareAndSendFriendInvitationEmail(recipient, userName, friendName, invitationLink);
            });
        } catch (MailException e) {
            throw new EmailSendException("Could not send email to ", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            emailExecutor.shutdown();
        }

        return new ResponseEntity<>(new InfoResponse("Invitation sent successfully"), HttpStatus.OK);

    }

    @PostMapping("/profile/update/password")
    public ResponseEntity updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, Principal principal) {
        if (updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getPasswordConfirmation())) {

            if (userService.updatePassword(principal.getName(), updatePasswordDTO.getOldPassword(), updatePasswordDTO.getNewPassword())) {
                return new ResponseEntity<>(new InfoResponse("Password changed successfully"), HttpStatus.OK);
            } else return new ResponseEntity<>(new InfoResponse("Invalid old password"), HttpStatus.BAD_REQUEST);

        } else
            return new ResponseEntity<>(new InfoResponse("Password and passwordConfirmation are not equals"), HttpStatus.BAD_REQUEST);

    }
}
