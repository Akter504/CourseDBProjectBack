package ru.maryan.webproject.coursedbprojectback.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.maryan.webproject.coursedbprojectback.components.JwtTokenProvider;
import ru.maryan.webproject.coursedbprojectback.models.Projects;
import ru.maryan.webproject.coursedbprojectback.services.ProjectsService;

import java.util.List;

@RestController
@RequestMapping("api/projects")
public class ProjectController {
    private final ProjectsService projectsService;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    public ProjectController(ProjectsService projectsService, JwtTokenProvider jwtTokenProvider) {
        this.projectsService = projectsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping()
    public ResponseEntity<List<Projects>> getProjects() {
        return ResponseEntity.ok(projectsService.getAllProjects());
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createProject(@RequestBody Projects projects,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.extractEmail(token);
        log.info("Creating project with name: {} and description: {} for user: {}", projects.getNameProject(), projects.getDescription(), email);
        projectsService.createProject(projects.getNameProject(), projects.getDescription(), email);
        log.info("Project created successfully");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        projectsService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
