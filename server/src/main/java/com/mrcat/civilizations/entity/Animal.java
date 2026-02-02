package com.mrcat.civilizations.entity;

import java.util.List;
import java.util.ArrayList;
import com.mrcat.civilizations.resources.Item;

public class Animal extends Entity {
    
    public List<Item> loot = new ArrayList<>();
    
    public Animal(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int flyingSpeed, int size, List<Item> loot) {
        super(name, health, damage, groundSpeed, swimmingSpeed, flyingSpeed, size);
        this.loot = loot;
    }
}