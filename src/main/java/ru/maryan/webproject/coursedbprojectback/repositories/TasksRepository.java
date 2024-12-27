package ru.maryan.webproject.coursedbprojectback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maryan.webproject.coursedbprojectback.models.Projects;
import ru.maryan.webproject.coursedbprojectback.models.Tasks;

import java.util.List;
import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {
   Optional<List<Tasks>> findAllByProject(Projects project);
}
