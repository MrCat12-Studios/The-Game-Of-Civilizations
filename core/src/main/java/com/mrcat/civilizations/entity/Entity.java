package com.mrcat.civilizations.entity;

import java.util.Map;
import java.util.HashMap;
import com.mrcat.civilizations.map.*;
import com.mrcat.civilizations.IO.Json;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Entity {

    public String name;
    public String id;
    public int maxHealth;
    public int health;
    public int damage;
    public int groundSpeed;
    public int swimmingSpeed;
    public int size;
    public Position position;
    public String ethnicity = "";
    public int sight;
    public Entity rider;
    public Json json;
    public boolean isUpdated = true;

    public static Map<String, Entity> entities = new HashMap<>();
    public static final Entity DEFAULT = new Entity("default", 0, 0, 0, 0, 0, 0);

    public Entity(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight) {
        this.name = name;
        maxHealth = health;
        this.health = health;
        this.damage = damage;
        this.groundSpeed = groundSpeed;
        this.swimmingSpeed = swimmingSpeed;
        this.size = size;
        this.sight = sight;
        for (int i = 0; i < 1000000; i++) {
            String formated = String.format("%06d", i);
            if (!Entity.entities.keySet().contains(formated)) {
                id = formated;
                break;
            }
        }
        updateJson();
        Entity.entities.put(id, this);
    }
    
    public void updateJson() {
        json.attributes.put("maxHealth", new JsonPrimitive(maxHealth));
        json.attributes.put("health", new JsonPrimitive(health));
        json.attributes.put("damage", new JsonPrimitive(damage));
        json.attributes.put("groundSpeed", new JsonPrimitive(groundSpeed));
        json.attributes.put("swimmingSpeed", new JsonPrimitive(swimmingSpeed));
        json.attributes.put("size", new JsonPrimitive(size));
        json.attributes.put("ethnicity", new JsonPrimitive(ethnicity));
        json.attributes.put("sight", new JsonPrimitive(sight));
        JsonArray coords = new JsonArray();
        coords.add(position.x);
        coords.add(position.y);
        json.attributes.put("position", coords);
        JsonObject riderObj = new JsonObject();
        for (String i : rider.json.attributes.keySet()) riderObj.add(i, rider.json.attributes.get(i));
        json.attributes.put("rider", riderObj);
        isUpdated = true;
    }

    public void setPosition(int x, int y) {
        position = new Position(x, y);
        World.chunks[y][x].units.add(this);
        isUpdated = true;
    }

    public void destroy() {
        Entity.entities.remove(this);
        World.chunks.
        isUpdated = true;
    }
    
    public void getDamaged(int damage) {
        health -= damage;
        if (health < 1) destroy();
        isUpdated = true;
    }

    public boolean inRange(Position pos) {
        return inRange(pos, sight);
    }

    public static boolean inRange(Position pos, int radius) {
        int x = pos.x;
        int y = pos.y;
        return x <= radius && y <= radius;
    }
}