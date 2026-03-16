package com.mrcat.civilizations.entity;

public class Rideable extends Entity {
    
    public Entity rider;
    
    public Rideable(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight) {
        super(name, health, damage, groundSpeed, swimmingSpeed, size, sight);
    }
    
    public void addRider(Entity rider) {
        this.rider = rider;
    }
}