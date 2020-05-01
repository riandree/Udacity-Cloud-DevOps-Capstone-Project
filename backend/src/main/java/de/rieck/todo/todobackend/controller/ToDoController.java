package de.rieck.todo.todobackend.controller;

import de.rieck.todo.todobackend.model.ToDoModel;
import de.rieck.todo.todobackend.service.ToDoPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.utils.Validate;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(path = "/api/v1/todo")
public class ToDoController {

    @Autowired
    private ToDoPersistenceService persistenceService;

    @GetMapping
    public List<ToDoModel> getAllToDosForAuthenticatedOwner() {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        return persistenceService.getToDosByOwner(owner);
    }

    @DeleteMapping(path = "{id}")
    public void deleteToDoById(@PathVariable String id) {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        persistenceService.deleteByIdAndOwner(id, owner);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ToDoModel createModelWithHeadlineAndDescription(@RequestBody Map<String, Object> params) {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        Validate.notNull(params.get("headline"), "headline not present");
        Validate.notNull(params.get("shortDesc"), "shortDesc not present");
        return persistenceService.createToDo(params.get("headline").toString(), params.get("shortDesc").toString(), owner);
    }

    @PutMapping(path = "{id}")
    public void updateModel(@PathVariable String id, @RequestBody ToDoModel model) {
        Validate.isTrue(model.getId().equals(id), "Payload id doesn't match path id");
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        persistenceService.updateToDo(model, owner);
    }

}
