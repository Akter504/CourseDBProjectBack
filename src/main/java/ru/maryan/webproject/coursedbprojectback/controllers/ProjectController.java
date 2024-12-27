package ru.maryan.webproject.coursedbprojectback.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
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
    public ResponseEntity<List<Projects>> getProjects(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.extractEmail(token);
        List<Projects> projects = projectsService.getProjectByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Projects not found"));
        return ResponseEntity.ok(projects);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<Projects>> adminGetProjects() {
        List<Projects> projects = projectsService.getAllProjects()
                .orElseThrow(() -> new IllegalArgumentException("Projects not found"));
        return ResponseEntity.ok(projects);
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

    @PutMapping("{id}")
    public ResponseEntity<String> updateProject(@PathVariable("id") Long id,
                                                @RequestBody Projects projects,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.extractEmail(token);
        List<GrantedAuthority> roles = jwtTokenProvider.extractAuthorities(token);
        if (projects.getCreatedBy().equals(email) || roles.get(0).getAuthority().equals("ROLE_ADMIN")) {
            projectsService.updateProject(id, projects.getDescription(), projects.getNameProject());
            return ResponseEntity.ok("Project updated");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't edit this Project");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable("id") Long id,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.extractEmail(token);
        Projects projects = projectsService.findProjectById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        List<GrantedAuthority> roles = jwtTokenProvider.extractAuthorities(token);
        if (projects.getCreatedBy().equals(email) || roles.get(0).getAuthority().equals("ROLE_ADMIN")) {
            projectsService.deleteProject(id);
            return ResponseEntity.ok("Project deleted");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't delete this project");
    }
}
