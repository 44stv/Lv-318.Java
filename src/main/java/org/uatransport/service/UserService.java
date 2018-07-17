package org.uatransport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.uatransport.entity.User;
import org.uatransport.entity.dto.LoginDTO;
import org.uatransport.entity.dto.UserDTO;

import java.security.GeneralSecurityException;
import java.security.Principal;

public interface UserService {

    User update(User user);

    User getById(Integer id);

    void deleteById(int id);

    User getUser(Principal principal);

    String signin(LoginDTO loginDTO);

    void signup(UserDTO user);

    User getUserByEmail(String userEmail);

    void activateUserByEmail(String userEmail);

    User updateUserRole(String role, String email);

    boolean existUserByEmail(String email);

    void deleteByEmail(String userEmail);

    boolean updatePassword(String name, String oldPassword, String newPassword);

    String singInWithSocialGoogle(UserDTO userDTO) throws GeneralSecurityException;

    String singInWithSocialFacebook(UserDTO userDTO);

    void updateUserEncodedPassword(String newPassword, String userEmail);

    Page<User> getAllUsers(Pageable page);

    Page<User> getByRole(String role, Pageable pageable);

}
