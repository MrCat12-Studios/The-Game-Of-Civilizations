package com.mrcat.civilizations.entity;

public class Building extends Entity {
    
    int buildDuration;
    
    public Building(String name, int health, int damage, int size, int buildDuration) {
        super(name, health, damage, 0, 0, 0, size);
        this.buildDuration = buildDuration;
    }
}