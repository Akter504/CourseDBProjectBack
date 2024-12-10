package ru.maryan.webproject.coursedbprojectback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maryan.webproject.coursedbprojectback.models.Projects;
import ru.maryan.webproject.coursedbprojectback.models.Tasks;
import ru.maryan.webproject.coursedbprojectback.repositories.TasksRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class TasksService {
    private final TasksRepository tasksRepository;

    @Autowired
    public TasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public Tasks createTask(String nameTask, String description,
                            String createdBy, Projects project)
    {
        Tasks task = new Tasks();
        LocalDateTime now = LocalDateTime.now();
        task.setNameTask(nameTask);
        task.setDescription(description);
        task.setCreatedBy(createdBy);
        task.setProject(project);
        task.setDueDate(now);
        return tasksRepository.save(task);
    }

    public void deleteTask(Long id) {
        tasksRepository.deleteById(id);
    }

    public List<Tasks> getAllTasks(Projects project) {
        return tasksRepository.findAllByProject(project);
    }
}
