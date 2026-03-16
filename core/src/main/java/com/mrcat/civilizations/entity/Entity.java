package com.mrcat.civilizations.entity;

import java.util.Map;
import java.util.HashMap;
import com.mrcat.civilizations.map.*;

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
    public String ethnicity;
    public int sight;
    public int energy;

    public static Map<String, Entity> entities = new HashMap<>();

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
        Entity.entities.put(id, this);
    }

    public Entity() {}

    public void moveTo(int x, int y) {
        
    }

    public void moveTo(Position position) {
        
    }

    public void destroy() {
        Entity.entities.remove(this);
    }
}