package org.uatransport.service.implementation;

import lombok.RequiredArgsConstructor;
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

import java.security.Principal;

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
    private AuthenticationManager authenticationManager;

    public String signin(LoginDTO loginDTO) {
        String username = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByEmail(username).getRole(),userRepository.findByEmail(username).getId());
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
    public String singUpWithSocial(UserDTO userDTO) {
        User user = new User();
        String[] splitStr = userDTO.getFirstName().split("\\s+");
        userDTO.setFirstName(splitStr[0].trim());
        user.setEmail(userDTO.getEmail());
        user.setRole(Role.USER);
        try {
            user.setLastName(splitStr[1].trim());
        } catch (ArrayIndexOutOfBoundsException e) {
            user.setLastName(splitStr[0].trim());
        }
        user.setPassword(userDTO.getPassword());
        user.setProvider(userDTO.getProvider());
        userRepository.save(user);
        return jwtTokenProvider.createToken(user.getEmail(), user.getRole(),user.getId());
    }
    @Override
    public String singInWithSocial(UserDTO userDTO) {
        String username = userDTO.getEmail();
        String provider = userDTO.getProvider();
        String password = userDTO.getPassword();
        if (userRepository.findProviderByEmail(username).equalsIgnoreCase(provider)) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                return jwtTokenProvider.createToken(username, userRepository.findByEmail(username).getRole(), userRepository.findByEmail(username).getId());
            } catch (AuthenticationException e) {
                throw new SecurityJwtException("Can`t login", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        return "Can`t login";
    }

    @Override
    @Transactional
    public void deleteByEmail(String userEmail) {

        System.out.println(userRepository.findByEmail(userEmail));

        if(userRepository.findByEmail(userEmail).getRole() == Role.UNACTIVATED){
            userRepository.deleteByEmail(userEmail);
        }
    }

    @Override
    public boolean updatePassword(String name, String oldPassword, String newPassword) {

        if(bcryptEncoder.matches(oldPassword,userRepository.findByEmail(name).getPassword())){
         userRepository.save(userRepository.findByEmail(name).setPassword(bcryptEncoder.encode(newPassword)));
         return true;
        }else return false;

    }
}
