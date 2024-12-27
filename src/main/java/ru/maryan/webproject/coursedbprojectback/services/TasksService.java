package ru.maryan.webproject.coursedbprojectback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maryan.webproject.coursedbprojectback.models.Projects;
import ru.maryan.webproject.coursedbprojectback.models.TaskStatus;
import ru.maryan.webproject.coursedbprojectback.models.Tasks;
import ru.maryan.webproject.coursedbprojectback.repositories.TaskStatusRepository;
import ru.maryan.webproject.coursedbprojectback.repositories.TasksRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TasksService {
    private final TasksRepository tasksRepository;
    private final TaskStatusRepository taskStatusRepository;

    @Autowired
    public TasksService(TasksRepository tasksRepository, TaskStatusRepository taskStatusRepository) {
        this.tasksRepository = tasksRepository;
        this.taskStatusRepository = taskStatusRepository;
    }

    public Tasks createTask(String nameTask, String description,
                            String createdBy, Projects project)
    {
        Tasks task = new Tasks();
        TaskStatus taskStatus = taskStatusRepository.findByName("TO_DO")
                .orElseThrow(() -> new IllegalArgumentException("Status not found"));
        LocalDateTime now = LocalDateTime.now();
        task.setNameTask(nameTask);
        task.setDescription(description);
        task.setCreatedBy(createdBy);
        task.setProject(project);
        task.setDueDate(now);
        task.setTaskStatus(taskStatus);
        return tasksRepository.save(task);
    }

    public void deleteTask(Long id) {
        tasksRepository.deleteById(id);
    }

    public TaskStatus updateTask(Long id, String nameTask, String description, String status) {
        Tasks task = tasksRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        TaskStatus taskStatus = taskStatusRepository.findByName(status)
                .orElseThrow(() -> new IllegalArgumentException("Status not found"));
        task.setNameTask(nameTask);
        task.setDescription(description);
        task.setTaskStatus(taskStatus);
        tasksRepository.save(task);
        return taskStatus;
    }

    public Optional<List<Tasks>> getAllTasks(Projects project) {
        return tasksRepository.findAllByProject(project);
    }

    public Optional<List<Tasks>> getAllTasksInDB() {
        return Optional.of(tasksRepository.findAll());
    }
}
