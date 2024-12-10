package ru.maryan.webproject.coursedbprojectback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maryan.webproject.coursedbprojectback.models.Projects;
import ru.maryan.webproject.coursedbprojectback.repositories.ProjectsRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectsService {
    private final ProjectsRepository projectsRepository;

    @Autowired
    public ProjectsService(ProjectsRepository projectsRepository) {
        this.projectsRepository = projectsRepository;
    }

    public Projects createProject(String nameProject, String description,
                              String createdBy) {
        Projects projects = new Projects();
        LocalDateTime now = LocalDateTime.now();
        projects.setNameProject(nameProject);
        projects.setCreatedBy(createdBy);
        projects.setCreatedAt(now);
        projects.setDescription(description);
        return projectsRepository.save(projects);
    }

    public void deleteProject(Long id) {
        projectsRepository.deleteById(id);
    }

    public Optional<Projects> findProjectById(Long projectId) {
        return projectsRepository.findById(projectId);
    }

    public List<Projects> getAllProjects() {
        return projectsRepository.findAll();
    }
}
