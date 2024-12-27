package ru.maryan.webproject.coursedbprojectback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maryan.webproject.coursedbprojectback.models.Projects;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects, Long> {
}
