package ru.maryan.webproject.coursedbprojectback.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Projects")
@Getter
@Setter
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name_project", nullable = false, length = 50)
    private String nameProject;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_by", nullable = false, length = 40)
    private String createdBy;

    @Column(name = "created_at", nullable = false, length = 50)
    private LocalDateTime createdAt;
}
