package de.rieck.todo.todobackend.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ToDoModel {

    public static String ToDo_TABLE_NAME = "TodoObjects";

    private String id;
    private String headline;
    private String shortDesc;
    private String owner;
    private List<ToDoItem> items;

    public ToDoModel(String id, String headline, String shortDesc, String owner, List<ToDoItem> items) {
        this.id = id;
        this.headline = headline;
        this.shortDesc = shortDesc;
        this.owner = owner;
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDoModel toDoModel = (ToDoModel) o;
        return id.equals(toDoModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ToDoModel{" +
                "id='" + id + '\'' +
                ", headline='" + headline + '\'' +
                ", shortDesc='" + shortDesc + '\'' +
                ", owner='" + owner + '\'' +
                ", items=" + items +
                '}';
    }

    public Map<String, AttributeValue> toDBMap(final ObjectMapper mapper) {
        String itemsJSON = items.stream()
                .map(item -> item.toJSONString(mapper))
                .collect(Collectors.joining(",", "[", "]"));
        return new HashMap<>() {
            {
                put("id", AttributeValue.builder().s(id).build());
                put("owner", AttributeValue.builder().s(owner).build());
                put("headline", AttributeValue.builder().s(headline).build());
                put("shortDesc", AttributeValue.builder().s(shortDesc).build());
                put("items", AttributeValue.builder().s(itemsJSON).build());
            }
        };
    }

    public static ToDoModel fromDBMap(ObjectMapper mapper, Map<String, AttributeValue> attributeValueMap) {
        String id=attributeValueMap.get("id").s();
        String owner=attributeValueMap.get("owner").s();
        String headline=attributeValueMap.get("headline").s();
        String shortDesc=attributeValueMap.get("shortDesc").s();
        List<ToDoItem> items=ToDoItem.fromJSONString(mapper,attributeValueMap.get("items").s());
        return new ToDoModel(id,headline,shortDesc,owner,items);
    }

    public String getId() {
        return id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<ToDoItem> getItems() {
        return items;
    }

    public void setItems(List<ToDoItem> items) {
        this.items = items;
    }
}
