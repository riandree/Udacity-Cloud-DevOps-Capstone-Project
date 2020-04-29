package de.rieck.todo.todobackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.rieck.todo.todobackend.model.ToDoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ToDoPersistenceService {

    @Autowired
    private DynamoDbClient dynamoDbClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ToDoModel createToDo(String headline, String description, String owner) {
        ToDoModel toDoModel = new ToDoModel(UUID.randomUUID().toString(), headline, description, owner, new LinkedList<>());

        dynamoDbClient.putItem(mkPutItemRequest(toDoModel));
        return toDoModel;
    }

    public List<ToDoModel> getToDosByOwner(String owner) {
        QueryRequest queryReq = QueryRequest.builder()
                .tableName(ToDoModel.ToDo_TABLE_NAME)
                .expressionAttributeValues(ToDoModel.queryByOwnerDBMap(owner))
                .build();

            QueryResponse response = dynamoDbClient.query(queryReq);
            return response.items().stream()
                    .map(todosMap -> ToDoModel.fromDBMap(objectMapper,todosMap))
                    .collect(Collectors.toList());
    }

    public void updateToDo(ToDoModel todo) {
        dynamoDbClient.putItem(mkPutItemRequest(todo));
    }

    private PutItemRequest mkPutItemRequest(ToDoModel toDoModel) {
        return PutItemRequest.builder()
                .tableName(ToDoModel.ToDo_TABLE_NAME)
                .item(toDoModel.toDBMap(objectMapper))
                .build();
    }

}
