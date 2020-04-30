package de.rieck.todo.todobackend.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ToDoItem {

    private int seq;
    private boolean checked;
    private String name;

    public ToDoItem(int seq, boolean checked, String name) {
        this.seq = seq;
        this.checked = checked;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDoItem toDoItem = (ToDoItem) o;
        return seq == toDoItem.seq &&
                checked == toDoItem.checked &&
                Objects.equals(name, toDoItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, checked, name);
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "seq=" + seq +
                ", checked=" + checked +
                ", name='" + name + '\'' +
                '}';
    }

    public String toJSONString(ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("seq", seq);
        node.put("checked", checked);
        node.put("name", name);
        try {
            return mapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ToDoItem> fromJSONString(ObjectMapper mapper, String json) {
        try {
            final JsonNode arrNode = new ObjectMapper().readTree(json);
            LinkedList<ToDoItem> items = new LinkedList<>();
            if (arrNode.isArray()) {
                for (final JsonNode node : arrNode) {
                    items.add(new ToDoItem(node.get("seq").asInt(), node.get("checked").asBoolean(), node.get("name").asText()));
                }
            } else {
                items.add(new ToDoItem(arrNode.get("seq").asInt(), arrNode.get("checked").asBoolean(), arrNode.get("name").asText()));
            }
            return items;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
