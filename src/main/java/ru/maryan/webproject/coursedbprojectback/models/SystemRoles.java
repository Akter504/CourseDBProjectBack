package ru.maryan.webproject.coursedbprojectback.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "System_Roles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name_system_role"})
)
@Getter
@Setter
public class SystemRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "name_system_role", nullable = false, length = 10)
    private String nameSystemRole;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private Users user;
}
