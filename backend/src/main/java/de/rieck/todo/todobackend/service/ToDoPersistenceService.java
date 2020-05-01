package de.rieck.todo.todobackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.rieck.todo.todobackend.model.ToDoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
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

    public void deleteByIdAndOwner(String id, String owner) {
        if (!getToDosByOwner(owner).stream().filter(todo -> todo.getId().equals(id)).findFirst().isPresent()) {
            throw new IllegalStateException("Invalid ID for ToDo");
        }

        HashMap<String, AttributeValue> keyToDelete = new HashMap<>();
        keyToDelete.put("id", AttributeValue.builder()
                .s(id)
                .build());

        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
                .tableName(ToDoModel.ToDo_TABLE_NAME)
                .key(keyToDelete)
                .build();

        try {
            dynamoDbClient.deleteItem(deleteReq);
        } catch (DynamoDbException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ToDoModel> getToDosByOwner(String owner) {
        HashMap<String, String> ownerAttrNameAlias = new HashMap<String, String>();
        ownerAttrNameAlias.put("#o", "owner");

        Map<String, AttributeValue> expressionAttrs = new HashMap<>();
        expressionAttrs.put(":owner", AttributeValue.builder().s(owner).build());

        QueryRequest queryReq = QueryRequest.builder()
                .tableName(ToDoModel.ToDo_TABLE_NAME)
                .indexName("TodoByUser")
                .keyConditionExpression("#o" + " = :owner")
                .expressionAttributeNames(ownerAttrNameAlias)
                .expressionAttributeValues(expressionAttrs)
                .build();

        QueryResponse response = dynamoDbClient.query(queryReq);
        return response.items().stream()
                .map(todosMap -> ToDoModel.fromDBMap(objectMapper, todosMap))
                .collect(Collectors.toList());
    }

    public void updateToDo(ToDoModel model, String owner) {
        Optional<ToDoModel> current = getToDosByOwner(owner).stream().filter(todo -> todo.getId().equals(model.getId())).findFirst();
        if (!current.isPresent()) {
            throw new IllegalStateException("ToDo to update not found");
        }
        if (!current.get().getOwner().equals(owner)) {
            throw new IllegalStateException("owners doesn't match");
        }

        dynamoDbClient.putItem(mkPutItemRequest(model));
    }

    private PutItemRequest mkPutItemRequest(ToDoModel toDoModel) {
        return PutItemRequest.builder()
                .tableName(ToDoModel.ToDo_TABLE_NAME)
                .item(toDoModel.toDBMap(objectMapper))
                .build();
    }

}
