package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.TokenModel;
import org.uatransport.entity.User;
import org.uatransport.entity.dto.LoginDTO;
import org.uatransport.entity.dto.UserDTO;
import org.uatransport.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody UserDTO userDTO, HttpServletResponse response) {

        String token = userService.signup(userDTO);
        response.setHeader("Authorization", token);

        return ResponseEntity.ok(new TokenModel(token));
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
}
