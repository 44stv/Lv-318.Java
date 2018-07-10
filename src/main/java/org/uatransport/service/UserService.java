package org.uatransport.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.uatransport.entity.User;
import org.uatransport.entity.dto.LoginDTO;
import org.uatransport.entity.dto.UserDTO;

import java.security.Principal;

public interface UserService {

    User update(User user);

    void deleteById(int id);

    User getUser(Principal principal);

    String signin(LoginDTO loginDTO);

    String signup(UserDTO user);

    User getUserByEmail(String userEmail);

    void activateUserByEmail(String userEmail);

    void updateUserEncodedPassword(String newPassword, String userEmail);

    boolean existUserByEmail(String email);

}
