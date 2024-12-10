package ru.maryan.webproject.coursedbprojectback.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maryan.webproject.coursedbprojectback.models.SystemRoles;
import ru.maryan.webproject.coursedbprojectback.models.Users;
import ru.maryan.webproject.coursedbprojectback.repositories.SystemRolesRepository;

import java.util.Optional;

@Service
public class SystemRolesService {
    final private SystemRolesRepository systemRolesRepository;

    @Autowired
    public SystemRolesService(SystemRolesRepository systemRolesRepository) {
        this.systemRolesRepository = systemRolesRepository;
    }

    public Optional<SystemRoles> getRoleUserByUser(Users user) {
        return systemRolesRepository.findByUser(user);
    }
}
