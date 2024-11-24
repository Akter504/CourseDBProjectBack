package ru.maryan.webproject.coursedbprojectback;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateTest {
    public static void main(String[] args) {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();
            SessionFactory sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
            System.out.println("Hibernate SessionFactory created successfully!");
        } catch (Exception e) {
            System.err.println("SessionFactory creation failed: " + e.getMessage());
        }
    }
}