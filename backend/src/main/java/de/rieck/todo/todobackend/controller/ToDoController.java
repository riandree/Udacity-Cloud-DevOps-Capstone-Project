package de.rieck.todo.todobackend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/api/v1/todo")
@CrossOrigin(origins = {"*"}, allowCredentials = "true", allowedHeaders = {"*"})
public class ToDoController {


}
