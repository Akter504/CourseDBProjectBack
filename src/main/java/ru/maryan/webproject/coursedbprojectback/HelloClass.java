package ru.maryan.webproject.coursedbprojectback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloClass {

    @RequestMapping("/api/hello") // URL-адрес для React
    public String sayHello() {
        return "redirect:/test.html";
    }
}