package de.rieck.todo.todobackend;

import de.rieck.todo.todobackend.model.ToDoModel;
import de.rieck.todo.todobackend.service.ToDoPersistenceService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@Disabled
@SpringBootTest
class TodoBackendApplicationTests {

    @Autowired
    private ToDoPersistenceService persistenceService;

    @Test
    public void insertToDo2DynamoDB() {
        persistenceService.createToDo("firstTest", "Lorem Ipsum", "1d1e34ea-165a-4baf-a163-8af934c1a119");
    }

    @Test
    public void getToDoSByOwner() {
        Assert.isTrue(persistenceService.getToDosByOwner("1d1e34ea-165a-4baf-a163-8af934c1a119").size() > 0, "keine ToDos gefunden");
        for (ToDoModel toDoModel : persistenceService.getToDosByOwner("1d1e34ea-165a-4baf-a163-8af934c1a119")) {
            System.out.println(toDoModel);
        }
    }

    @Test
    public void deleteToDoByID() {
        ToDoModel toDelete = persistenceService.createToDo("firstTest", "Lorem Ipsum", "1d1e34ea-165a-4baf-a163-8af934c1a119");
        persistenceService.deleteByIdAndOwner(toDelete.getId(), "1d1e34ea-165a-4baf-a163-8af934c1a119");
    }

    @Test
    public void updateToDo() {
        ToDoModel toupdate = persistenceService.createToDo("firstTest", "Lorem Ipsum", "1d1e34ea-165a-4baf-a163-8af934c1a119");
        toupdate.setHeadline("UPDATED");
        persistenceService.updateToDo(toupdate, "1d1e34ea-165a-4baf-a163-8af934c1a119");
    }
}
