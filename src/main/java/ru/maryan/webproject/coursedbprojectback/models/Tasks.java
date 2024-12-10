package ru.maryan.webproject.coursedbprojectback.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Tasks")
@Getter
@Setter
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name_task", nullable = false, length = 50)
    private String nameTask;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_by", nullable = false, length = 40)
    private String createdBy;

    @Column(name = "due_date", nullable = false, length = 50)
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Projects project;
}
