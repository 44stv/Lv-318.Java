package org.uatransport.service.implementation;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.Role;
import org.uatransport.entity.User;
import org.uatransport.entity.dto.LoginDTO;
import org.uatransport.entity.dto.UserDTO;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.exception.SecurityJwtException;
import org.uatransport.repository.UserRepository;
import org.uatransport.security.JwtTokenProvider;
import org.uatransport.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Qualifier("UserDetails")
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final JacksonFactory jacksonFactory = new JacksonFactory();

    private final ApacheHttpTransport transport = new ApacheHttpTransport();

    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)

        .setAudience(Collections.singletonList("1021227322496-q7977jujlatadfoql9skbeasai2550mn.apps.googleusercontent.com"))
        .build();

    public String signin(LoginDTO loginDTO) {
        String username = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        Role role = userRepository.findByEmail(username).getRole();
        Integer id = userRepository.findByEmail(username).getId();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, role, id);
        } catch (AuthenticationException e) {
            throw new SecurityJwtException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);

        }
    }

    public boolean signup(UserDTO userDTO) {

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.UNACTIVATED);
        userRepository.save(user);
        return true;

    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public void activateUserByEmail(String userEmail) {

        User user = userRepository.findByEmail(userEmail);
        user.setRole(Role.USER);
        userRepository.saveAndFlush(user);

    }

    @Override
    public void updateUserEncodedPassword(String newPassword, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        user.setPassword(newPassword);

        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Parameter should not be null");
        }
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUser(Principal principal) {

        return userRepository.findByEmail(principal.getName());

    }

    @Override
    @Transactional
    public User updateUserRole(String role, String email) {
        if (role == null) {
            throw new IllegalArgumentException("Parameter should not be null");
        }
        if (userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email);
            user.setRole(Role.valueOf(role.trim().toUpperCase()));
            return userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("User not found");
        }

    }

    @Override
    public boolean existUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Override
    public String singInWithSocialGoogle(UserDTO userDTO) {
        try {
            GoogleIdToken idToken = verifier.verify(userDTO.getTokenId());
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                if (userService.existUserByEmail(userDTO.getEmail())) {
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
                    return jwtTokenProvider.createToken(userDTO.getEmail(), userRepository.findByEmail(userDTO.getEmail()).getRole(),
                        userRepository.findByEmail(userDTO.getEmail()).getId());
                } else {
                    return addSocialUser(userDTO);
                }
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new SecurityJwtException("Can`t login", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return "Can`t login";
    }

    @Override
    public String singInWithSocialFacebook(UserDTO userDTO) {
        String email = userDTO.getEmail().trim();

        if (userRepository.existsByEmail(email)) {
            Role role = userRepository.findByEmail(email).getRole();
            Integer id = userRepository.findByEmail(email).getId();
            String password = userDTO.getPassword();
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                return jwtTokenProvider.createToken(email, role, id);
            } catch (AuthenticationException e) {
                System.out.println("ExMari");
                throw new SecurityJwtException("Can`t login with Facebook", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            return addSocialUser(userDTO);
        }
    }

    @Override
    @Transactional
    public void deleteByEmail(String userEmail) {

        if (userRepository.findByEmail(userEmail).getRole() == Role.UNACTIVATED) {
            userRepository.deleteByEmail(userEmail);
        }
    }

    @Override
    public boolean updatePassword(String name, String oldPassword, String newPassword) {
        if (bcryptEncoder.matches(oldPassword, userRepository.findByEmail(name).getPassword())) {
            userRepository.save(userRepository.findByEmail(name).setPassword(bcryptEncoder.encode(newPassword)));
            return true;
        } else {
            return false;
        }
    }


    private String addSocialUser(UserDTO userDTO) {
        User user = new User();
        String email = userDTO.getEmail();
        String name = userDTO.getFirstName();
        String[] splitStr = name.split("\\s+");
        user.setFirstName(splitStr[0].trim());
        user.setEmail(email);
        user.setRole(Role.USER);
        try {
            user.setLastName(splitStr[1].trim());
        } catch (ArrayIndexOutOfBoundsException e) {
            user.setLastName(splitStr[0].trim());
        }
        user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
        user.setProvider(userDTO.getProvider());
        userRepository.save(user);
        return jwtTokenProvider.createToken(email, user.getRole(), userRepository.findByEmail(email).getId());
    }
}
