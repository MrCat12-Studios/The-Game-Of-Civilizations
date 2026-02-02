package com.mrcat.civilizations.entity;

import com.mrcat.civilizations.map.Position;

public class Entity {
    
    public String name;
    public int maxHealth;
    public int health;
    public int damage;
    public int groundSpeed;
    public int swimmingSpeed;
    public int flyingSpeed;
    public int size;
    public Position position;
    public String Ethnicity;

    public Entity(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int flyingSpeed, int size) {
        this.name = name;
        maxHealth = health;
        this.health = health;
        this.damage = damage;
        this.groundSpeed = groundSpeed;
        this.swimmingSpeed = swimmingSpeed;
        this.flyingSpeed = flyingSpeed;
        this.size = size;
    }
    
    public Entity() {}
    
    public void moveTo(Position position) {
        
    }
}