package ru.maryan.webproject.coursedbprojectback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maryan.webproject.coursedbprojectback.models.SystemRoles;
import ru.maryan.webproject.coursedbprojectback.models.Users;

import java.util.Optional;

@Repository
public interface SystemRolesRepository extends JpaRepository<SystemRoles, Long> {
    Optional<SystemRoles> findByUser(Users user);
}
