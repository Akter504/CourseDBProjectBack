package ru.maryan.webproject.coursedbprojectback.controllers;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maryan.webproject.coursedbprojectback.components.JwtTokenProvider;
import ru.maryan.webproject.coursedbprojectback.models.Projects;
import ru.maryan.webproject.coursedbprojectback.models.TaskStatus;
import ru.maryan.webproject.coursedbprojectback.models.Tasks;
import ru.maryan.webproject.coursedbprojectback.services.ProjectsService;
import ru.maryan.webproject.coursedbprojectback.services.TasksService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final TasksService tasksService;
    private final ProjectsService projectsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public TaskController(TasksService tasksService, ProjectsService projectsService, JwtTokenProvider jwtTokenProvider) {
        this.tasksService = tasksService;
        this.projectsService = projectsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("{project_id}")
    public ResponseEntity<List<Tasks>> getProjects(@PathVariable("project_id") Long projectId) {
        Optional<Projects> existingProjects = projectsService.findProjectById(projectId);
        if (existingProjects.isPresent()) {
            Projects project = existingProjects.get();
            List<Tasks> tasks = tasksService.getAllTasks(project)
                    .orElseThrow(() -> new IllegalArgumentException("Tasks not found"));
            return ResponseEntity.ok(tasks);
        }
        return ResponseEntity.badRequest().build();

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<Tasks>> adminGetProjects() {
        List<Tasks> tasks = tasksService.getAllTasksInDB()
                .orElseThrow(() -> new IllegalArgumentException("Tasks not found"));
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/new/{project_id}")
    public ResponseEntity<Map<String, Object>> createTask(@PathVariable("project_id") Long projectId,
                                                          @RequestBody Tasks tasks,
                                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.extractEmail(token);

        Optional<Projects> existingProjects = projectsService.findProjectById(projectId);
        Map<String, Object> response = new HashMap<>();
        if (existingProjects.isPresent()) {
            Projects project = existingProjects.get();
            log.info("Creating task with name: {} and description: {} for user: {}", tasks.getNameTask(), tasks.getDescription(), email);
            tasksService.createTask(tasks.getNameTask(), tasks.getDescription(),
                    email, project);
            log.info("Successful create task.");

            response.put("success", true);
            response.put("message", "The task has been successfully created.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        log.error("NO SUCH PROJECT");
        response.put("success", false);
        response.put("message", "Project not find.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        tasksService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}/{status}")
    public ResponseEntity<TaskStatus> updateTask(@PathVariable("id") Long id,@PathVariable("status") String status, @RequestBody Tasks tasks) {
        TaskStatus updatedStatus = tasksService.updateTask(id, tasks.getNameTask(), tasks.getDescription(), status);
        return ResponseEntity.ok(updatedStatus);
    }

}
