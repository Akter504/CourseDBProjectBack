package ru.maryan.webproject.coursedbprojectback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maryan.webproject.coursedbprojectback.models.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}
