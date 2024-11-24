package ru.maryan.webproject.coursedbprojectback.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.maryan.webproject.coursedbprojectback.models.Users;
import ru.maryan.webproject.coursedbprojectback.services.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Users user) {
        Optional<Users> existingUser = userService.getUserByEmail(user.getEmail());
        Map<String, Object> response = new HashMap<>();
        if (existingUser.isPresent()) {
            response.put("success", false);
            response.put("message", "Email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        userService.createUser(user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getSurnameUser(),
                user.getPasswordHash());
        response.put("success", true);
        response.put("message", "User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Users user) {
        Optional<Users> existingUser = userService.getUserByEmail(user.getEmail());
        Map<String, Object> response = new HashMap<>();
        if (existingUser.isPresent()) {
            Users foundUser = existingUser.get();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (passwordEncoder.matches(user.getPasswordHash(), foundUser.getPasswordHash())) {
                response.put("success", true);
                response.put("message", "Successfully logged in");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } else {
            response.put("success", false);
            response.put("message", "Email not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
