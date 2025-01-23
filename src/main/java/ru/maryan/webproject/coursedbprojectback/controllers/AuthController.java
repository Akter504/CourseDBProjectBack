package ru.maryan.webproject.coursedbprojectback.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.maryan.webproject.coursedbprojectback.components.JwtTokenProvider;
import ru.maryan.webproject.coursedbprojectback.models.SystemRoles;
import ru.maryan.webproject.coursedbprojectback.models.Users;
import ru.maryan.webproject.coursedbprojectback.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(UserService userService, BCryptPasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Users user) {
        Optional<Users> existingUser = userService.getUserByEmail(user.getEmail());
        Map<String, Object> response = new HashMap<>();
        if (existingUser.isPresent()) {
            response.put("success", false);
            response.put("message", "Email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Users savedUser = userService.createUser(user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getSurnameUser(),
                user.getPasswordHash());
        Optional<SystemRoles> roles = userService.getUserRole(savedUser);
        if (roles.isPresent()) {
            String token = jwtTokenProvider.createToken(savedUser.getUserName(), savedUser.getEmail(),
                    List.of(roles.get().getNameSystemRole()));
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("token" , token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        response.put("success", false);
        response.put("message", "Unknown role in DB");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Users user) {
        Optional<Users> existingUser = userService.getUserByEmail(user.getEmail());
        Map<String, Object> response = new HashMap<>();
        if (existingUser.isPresent()) {
            Users foundUser = existingUser.get();
            if (passwordEncoder.matches(user.getPasswordHash(), foundUser.getPasswordHash())) {

                Optional<SystemRoles> roles = userService.getUserRole(foundUser);
                if (roles.isPresent()) {
                    String token = jwtTokenProvider.createToken(foundUser.getUserName(), foundUser.getEmail(),
                            List.of(roles.get().getNameSystemRole()));
                    System.out.println("Token is created\n");
                    response.put("success", true);
                    response.put("message", "User authorization successfully");
                    response.put("token" , token);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        response.put("success", false);
        response.put("message", "Email not found");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/test-jwt")
    public ResponseEntity<String> privateResource() {
        return ResponseEntity.ok("This is a private resource");
    }
}
