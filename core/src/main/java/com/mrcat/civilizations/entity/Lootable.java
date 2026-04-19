package com.mrcat.civilizations.entity;

import java.util.List;
import java.util.ArrayList;
import com.mrcat.civilizations.resources.Item;

public class Lootable extends Entity {
    
    public List<Item> loot = new ArrayList<>();
    
    public Lootable(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight, List<Item> loot) {
        super(name, health, damage, groundSpeed, swimmingSpeed, size, sight);
        this.loot = loot;
    }

    public void onDeath() {
        for (Item i : loot) new Collectable(i.name, i);
        isUpdated = true;
    }
}