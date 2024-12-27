package ru.maryan.webproject.coursedbprojectback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maryan.webproject.coursedbprojectback.models.Comments;
import ru.maryan.webproject.coursedbprojectback.models.Tasks;

import java.util.List;

import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    Optional<List<Comments>> findAllByTask(Tasks task);
}
