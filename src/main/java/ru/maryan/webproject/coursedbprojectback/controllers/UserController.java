package ru.maryan.webproject.coursedbprojectback.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maryan.webproject.coursedbprojectback.components.JwtTokenProvider;
import ru.maryan.webproject.coursedbprojectback.models.Users;
import ru.maryan.webproject.coursedbprojectback.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/{id}")
    public Optional<Users> getUser(@RequestBody Users user) {
        return userService.getUserById(user.getId());
    }

    @GetMapping("/profile")
    public Users getUserProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.extractEmail(token);
        Users user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPasswordHash("");
        return user;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public Map<String, Object> adminGetUsers() {
        Map<String, Object> users = userService.getAllUsers();
        return users;
    }

    @PostMapping("/new")
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getSurnameUser(),
                user.getPasswordHash());
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable("id") Long id, @RequestBody Users user) {
        userService.updateUser(id, user.getUserName(),
                user.getPhoneNumber(),
                user.getSurnameUser());

    }

    @PutMapping("/{id}/{role}")
    public void updateUserAndRole(@PathVariable("id") Long id,
                                  @PathVariable("role") String role,
                                  @RequestBody Users user) {
        userService.updateUserAndRole(id, user.getUserName(),
                user.getPhoneNumber(),
                user.getSurnameUser()
                , role);

    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }





}
