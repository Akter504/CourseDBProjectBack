package ru.maryan.webproject.coursedbprojectback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maryan.webproject.coursedbprojectback.models.TaskStatus;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    Optional<TaskStatus> findByName(String status);
}
