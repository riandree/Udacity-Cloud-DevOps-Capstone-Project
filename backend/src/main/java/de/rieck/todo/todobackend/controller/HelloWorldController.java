package de.rieck.todo.todobackend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/api/v1/hello")
public class HelloWorldController {

    @GetMapping()
    @CrossOrigin(origins = {"*"}, allowCredentials = "true", allowedHeaders = {"*"})
    public String sayHello() {
        return "Hello World";
    }
}
