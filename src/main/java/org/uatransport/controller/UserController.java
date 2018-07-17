package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.*;
import org.uatransport.entity.dto.*;
import org.uatransport.exception.EmailSendException;
import org.uatransport.exception.SecurityJwtException;
import org.uatransport.security.JwtTokenProvider;
import org.uatransport.service.TemporaryDataConfirmationService;
import org.uatransport.service.UserService;
import org.uatransport.service.UserValidatorService;
import org.uatransport.service.email.EmailService;
import org.uatransport.service.implementation.ExpirationCheckService;

import javax.servlet.http.HttpServletResponse;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor

public class UserController {

    @Value("${server.url}")
    private String serverUrl;

    @Value("${server.invitationLink}")
    private String invitationLink;

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TemporaryDataConfirmationService temporaryDataConfirmationService;
    private final EmailService emailService;
    private final ExpirationCheckService expirationCheckService;
    private final UserValidatorService userValidatorService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody UserDTO userDTO) {
        userValidatorService.validateUserOnRegistration(userDTO);

        if (userValidatorService.validateForUnactivating(userDTO.getEmail())) {

            userService.deleteByEmail(userDTO.getEmail());
        }

        userService.signup(userDTO);

        final String uuid = UUID.randomUUID().toString().replace("-", "");
        final String confirmUrl = serverUrl + "/main/user/activate/" + uuid;
        String email = userDTO.getEmail();
        String firstName = userDTO.getFirstName();

        temporaryDataConfirmationService
                .save(temporaryDataConfirmationService.makeRegistrationConfirmationEntity(uuid, email));
        try {
            emailService.prepareAndSendConfirmRegistrationEmail(email, firstName, confirmUrl);
        } catch (MailException e) {
            throw new EmailSendException("Could not send email to " + email, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new InfoResponse("Please check your email, and confirm registration"));
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
                try {
                    emailService.prepareAndSendWelcomeEmail(user.getEmail(), user.getFirstName());
                } catch (MailException e) {
                    throw new EmailSendException("Could not send email to " + user.getEmail(),
                            HttpStatus.INTERNAL_SERVER_ERROR);
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
        userValidatorService.validateForActivating(loginDTO.getEmail());
        userValidatorService.validateUserOnLogin(loginDTO);
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

    @PostMapping("/forget/password/confirm")
    public ResponseEntity forgetPasswordSendConfirmation(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        userValidatorService.checkPasswords(forgetPasswordDTO.getPassword(),
                forgetPasswordDTO.getPasswordConfirmation());

        final String uuid = UUID.randomUUID().toString().replace("-", "");

        final String confirmUrl = serverUrl + "/main/user/forgetpass/" + uuid;
        String userEmail = forgetPasswordDTO.getEmail();
        String firstName = userService.getUserByEmail(userEmail).getFirstName();

        temporaryDataConfirmationService.save(temporaryDataConfirmationService.makePasswordConfirmationEntity(uuid,
                forgetPasswordDTO.getPassword(), userEmail));
        try {
            emailService.prepareAndSendConfirmPassEmail(userEmail, firstName, confirmUrl);
        } catch (MailException e) {
            throw new EmailSendException("Could not send email to " + userEmail, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new InfoResponse("Please, check your email "), HttpStatus.OK);

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

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new InfoResponse("You are successfully updated password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new InfoResponse("Error during password changing"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InfoResponse("Error during password changing"));
    }

    @PostMapping("/social")
    public ResponseEntity socialSignIn(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        try {
            String token = userService.singInWithSocialGoogle(userDTO);
            response.setHeader("Authorization", token);

            return ResponseEntity.ok(new TokenModel(token));
        } catch (GeneralSecurityException e) {
            throw new SecurityJwtException("Can`t login", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/socialFacebook")
    public ResponseEntity socialFacebookSignIn(@RequestBody UserDTO userDTO, HttpServletResponse response) {

        String token = userService.singInWithSocialFacebook(userDTO);
        response.setHeader("Authorization", token);
        return ResponseEntity.ok(new TokenModel(token));
    }

    @PostMapping("/invite")
    public ResponseEntity inviteFriend(@RequestBody FriendInvitationDTO friendInvitationDTO, Principal principal) {
        String friendEmail = friendInvitationDTO.getFriendEmail();
        String userName = userService.getUser(principal).getFirstName() + " "
                + userService.getUser(principal).getLastName();
        String friendName = friendInvitationDTO.getFriendName();

        try {
            emailService.prepareAndSendFriendInvitationEmail(friendEmail, userName, friendName, invitationLink);
        } catch (MailException e) {
            throw new EmailSendException("Could not send email to ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new InfoResponse("Invitation sent successfully"), HttpStatus.OK);

    }

    @PostMapping("/profile/update/password")
    public ResponseEntity updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, Principal principal) {
        userValidatorService.checkPasswords(updatePasswordDTO.getNewPassword(),
                updatePasswordDTO.getPasswordConfirmation());
        if (userService.updatePassword(principal.getName(), updatePasswordDTO.getOldPassword(),
                updatePasswordDTO.getNewPassword())) {
            return new ResponseEntity<>(new InfoResponse("Password changed successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new InfoResponse("Invalid old password"), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/info/{id}")
    public UserInfo getUserInfoById(@PathVariable("id") Integer id) {
        return modelMapper.map(userService.getById(id), UserInfo.class);
    }

    @PutMapping("/update-role")
    public ResponseEntity updateUserRole(@RequestBody UpdateUserRoleDTO updateUserRoleDTO) {
        String role = updateUserRoleDTO.getRole();
        String email = updateUserRoleDTO.getEmail();
        return new ResponseEntity<>(userService.updateUserRole(role, email), HttpStatus.OK);
    }

    @GetMapping()
    public Page<AllUsersDTO> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable).map(user -> modelMapper.map(user, AllUsersDTO.class));
    }

    @GetMapping(params = "role")
    public Page<AllUsersDTO> getAllUsersByRole(@RequestParam("role") String role, Pageable pageable) {
        return userService.getByRole(role, pageable).map(user -> modelMapper.map(user, AllUsersDTO.class));
    }

}
