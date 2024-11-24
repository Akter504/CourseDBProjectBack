package ru.maryan.webproject.coursedbprojectback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maryan.webproject.coursedbprojectback.models.Users;
import ru.maryan.webproject.coursedbprojectback.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRep;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRep, BCryptPasswordEncoder passwordEncoder) {
        this.userRep = userRep;
        this.passwordEncoder = passwordEncoder;
    }

    public Users createUser(String userName, String email,
                            String phoneNumber, String surnameUser,
                            String passwordHash) {
        Users users = new Users();
        users.setUserName(userName);
        users.setEmail(email);
        users.setPasswordHash(passwordEncoder.encode(passwordHash));
        users.setPhoneNumber(phoneNumber);
        users.setSurnameUser(surnameUser);
        return userRep.save(users);
    }

    public Optional<Users> getUserById(Long id) {
        return userRep.findById(id);
    }

    public Users updateUser(Long id, String userName,
                            String email, String phoneNumber,
                            String surnameUser, String passwordHash) {
        Users user = userRep.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserName(userName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setSurnameUser(surnameUser);
        user.setPasswordHash(passwordEncoder.encode(passwordHash));
        return userRep.save(user);
    }

    public Optional<Users> getUserByEmail(String email) {
        return userRep.findByEmail(email);
    }

    public List<Users> getAllUsers() {
        return userRep.findAll();
    }

    public void deleteUser(Long id) {
        userRep.deleteById(id);
    }
}
