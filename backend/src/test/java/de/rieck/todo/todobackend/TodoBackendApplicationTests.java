package de.rieck.todo.todobackend;

import de.rieck.todo.todobackend.service.ToDoPersistenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TodoBackendApplicationTests {

	@Autowired
	private ToDoPersistenceService persistenceService;

	@Test
	void insertToDo2DynamoDB() {
		persistenceService.createToDo("firstTest","Lorem Ipsum","andre");
	}

}
