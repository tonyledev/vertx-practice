package com.tony.demo.vertx_starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonObjectExample {
  @Test
  void jsonObjectCanBeMapped() {
    final JsonObject myJsonObject = new JsonObject();
    myJsonObject.put("id", 1);
    myJsonObject.put("name", "tony");
    myJsonObject.put("loves_vertx", true);

    final String encoded = myJsonObject.encode();
    assertEquals("{\"id\":1,\"name\":\"tony\",\"loves_vertx\":true}", encoded);

    final JsonObject decodedJsonObject = new JsonObject(encoded);
    assertEquals(myJsonObject, decodedJsonObject);
  }

  @Test
  void jsonObjectCanBeCreatedFromMap() {
    final Map<String, Object>  myMap = new HashMap<>();
    myMap.put("id", 1);
    myMap.put("name", "tony");
    myMap.put("loves_vertx", true);

    final JsonObject asJsonObject = new JsonObject(myMap);
    assertEquals(myMap, asJsonObject.getMap());
    assertEquals(1, asJsonObject.getInteger("id"));
    assertEquals(true, asJsonObject.getBoolean("loves_vertx"));
  }

  @Test
  void jsonArrayCanBeMapped() {
    final JsonArray myJsonArray = new JsonArray();
    myJsonArray.add(new JsonObject().put("id", 1))
      .add(new JsonObject().put("id", 2))
      .add(new JsonObject().put("id", 3))
      .add("randomValue");
    assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3},\"randomValue\"]", myJsonArray.encode());
  }

  @Test
  void canMapJavaObjects() {
    final Person person = new Person(1, "tony", true);
    final JsonObject tony = JsonObject.mapFrom(person);
    assertEquals(person.getId(), tony.getInteger("id"));
    assertEquals(person.getName(), tony.getString("name"));
    assertEquals(person.isLovesVertx(), tony.getBoolean("lovesVertx"));

    final Person person2 = tony.mapTo(Person.class);
    assertEquals(person.getId(), person2.getId());
    assertEquals(person.getName(), person2.getName());
    assertEquals(person.isLovesVertx(), person2.isLovesVertx());
  }
}
