package de.rieck.todo.todobackend;

import de.rieck.todo.todobackend.model.ToDoModel;
import de.rieck.todo.todobackend.service.ToDoPersistenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class TodoBackendApplicationTests {

    @Autowired
    private ToDoPersistenceService persistenceService;

    @Test
    public void insertToDo2DynamoDB() {
        persistenceService.createToDo("firstTest", "Lorem Ipsum", "andre");
    }

    @Test
    public void getToDoSByOwner() {
        Assert.isTrue(persistenceService.getToDosByOwner("andre").size() > 0, "keine ToDos gefunden");
        for (ToDoModel toDoModel : persistenceService.getToDosByOwner("andre")) {
            System.out.println(toDoModel);
        }
    }

    @Test
    public void deleteToDoByID() {
        ToDoModel toDelete=persistenceService.createToDo("firstTest", "Lorem Ipsum", "andre");
        persistenceService.deleteById(toDelete.getId());
    }
}
