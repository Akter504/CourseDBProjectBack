package ru.maryan.webproject.coursedbprojectback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maryan.webproject.coursedbprojectback.models.Projects;
import ru.maryan.webproject.coursedbprojectback.models.Users;
import ru.maryan.webproject.coursedbprojectback.repositories.ProjectsRepository;
import ru.maryan.webproject.coursedbprojectback.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectsService {
    private final ProjectsRepository projectsRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectsService(ProjectsRepository projectsRepository, UserRepository userRepository) {
        this.projectsRepository = projectsRepository;
        this.userRepository = userRepository;
    }

    public Projects createProject(String nameProject, String description,
                              String createdBy) {
        Projects projects = new Projects();
        Users users = userRepository.findByEmail(createdBy)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        LocalDateTime now = LocalDateTime.now();
        projects.setNameProject(nameProject);
        projects.setCreatedBy(createdBy);
        projects.setCreatedAt(now);
        projects.setDescription(description);
        projects.setUser(users);
        return projectsRepository.save(projects);
    }

    public void deleteProject(Long id) {
        projectsRepository.deleteById(id);
    }

    public Optional<Projects> findProjectById(Long projectId) {
        return projectsRepository.findById(projectId);
    }

    public void updateProject(Long id, String description, String nameProject) {
        Projects projects = projectsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        projects.setDescription(description);
        projects.setNameProject(nameProject);
        projectsRepository.save(projects);
    }

    public Optional<List<Projects>> getAllProjects() {
        return Optional.of(projectsRepository.findAll());
    }

    public Optional<List<Projects>> getProjectByEmail(String email) {
        return projectsRepository.findAllByCreatedBy(email);
    }
}
