package ru.maryan.webproject.coursedbprojectback.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true, length = 30)
    private String userName;

    @Column(name = "email", nullable = false, unique = true, length = 40)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "surname_user", nullable = false, length = 20)
    private String surnameUser;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
}
