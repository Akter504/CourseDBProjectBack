package ru.maryan.webproject.coursedbprojectback.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.maryan.webproject.coursedbprojectback.models.Users;
import ru.maryan.webproject.coursedbprojectback.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<Users> getUsers(@RequestBody Users user) {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<Users> getUser(@RequestBody Users user) {
        return userService.getUserById(user.getId());
    }

    @PostMapping("/new")
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getSurnameUser(),
                user.getPasswordHash());
    }

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody Users user) {
        userService.updateUser(id, user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getSurnameUser(),
                user.getPasswordHash());

    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody Users user) {
        userService.deleteUser(user.getId());
    }





}
