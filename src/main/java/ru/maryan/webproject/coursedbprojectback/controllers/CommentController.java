package ru.maryan.webproject.coursedbprojectback.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.maryan.webproject.coursedbprojectback.components.JwtTokenProvider;
import ru.maryan.webproject.coursedbprojectback.models.Comments;
import ru.maryan.webproject.coursedbprojectback.models.Tasks;
import ru.maryan.webproject.coursedbprojectback.repositories.TasksRepository;
import ru.maryan.webproject.coursedbprojectback.services.CommentsService;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    private final CommentsService commentsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CommentController(CommentsService commentsService, JwtTokenProvider jwtTokenProvider) {
        this.commentsService = commentsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("title/{id}")
    public ResponseEntity<String> getSurname(@PathVariable("id") Long id) {
        String surname = commentsService.getName(id);
        return ResponseEntity.ok(surname);
    }

    @GetMapping("{task_id}")
    public ResponseEntity<List<Comments>> getComments(@PathVariable("task_id") Long taskId) {
        Map<String, Object> response = new HashMap<>();
        List<Comments> comments = commentsService.getComments(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Comments not found"));
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/new/{task_id}")
    public void createComment(@PathVariable("task_id") Long taskId,
                              @RequestBody Comments comments,
                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.extractEmail(token);
        commentsService.createComment(comments.getCommentText(), email, taskId);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateComment(@PathVariable("id") Long id,
                                                @RequestBody Comments comments,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.extractEmail(token);
        if (comments.getCreatedBy().equals(email)) {
            commentsService.updateComment(id, comments.getCommentText());
            return ResponseEntity.ok("Comment updated");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't edit this comment");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.extractEmail(token);
        Comments comments = commentsService.getComment(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found."));
        if (comments.getCreatedBy().equals(email)) {
            commentsService.deleteComment(id);
            return ResponseEntity.ok("Comment deleted");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't delete this comment");
    }
}
