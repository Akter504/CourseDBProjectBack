package ru.maryan.webproject.coursedbprojectback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maryan.webproject.coursedbprojectback.models.Comments;
import ru.maryan.webproject.coursedbprojectback.models.Tasks;
import ru.maryan.webproject.coursedbprojectback.repositories.CommentsRepository;
import ru.maryan.webproject.coursedbprojectback.repositories.TasksRepository;
import ru.maryan.webproject.coursedbprojectback.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final TasksRepository tasksRepository;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository, UserRepository userRepository, TasksRepository tasksRepository) {
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.tasksRepository = tasksRepository;
    }

    public void createComment(String commentText, String createdBy, Long taskId) {
        Comments comments = new Comments();
        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        LocalDateTime now = LocalDateTime.now();
        comments.setCommentText(commentText);
        comments.setCreatedAt(now);
        comments.setCreatedBy(createdBy);
        comments.setTask(task);
        commentsRepository.save(comments);
    }

    public void deleteComment(Long id) {
        commentsRepository.deleteById(id);
    }

    public void updateComment(Long id, String commentText) {
        Comments comments = commentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        comments.setCommentText(commentText);
        commentsRepository.save(comments);
    }

    public Optional<List<Comments>> getComments(Long taskId) {
        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return commentsRepository.findAllByTask(task);
    }

    public String getName(Long id) {
        String email = commentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"))
                .getCreatedBy();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found by email"))
                .getSurnameUser();
    }

    public Optional<Comments> getComment(Long id) {
        return commentsRepository.findById(id);
    }
}
