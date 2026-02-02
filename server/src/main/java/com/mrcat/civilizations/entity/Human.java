package com.mrcat.civilizations.entity;

import java.util.List;
import java.util.ArrayList;
import com.mrcat.civilizations.resources.Item;
import com.mrcat.civilizations.resources.Events;

public class Human extends Animal {
    
    private List<Item> inventory = new ArrayList<>();
    public int hand;
    
    public Human(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int flyingSpeed, int size) {
        super(name, health, damage, groundSpeed, swimmingSpeed, flyingSpeed, size, new ArrayList<>());
    }
    
    public Human(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int flyingSpeed, int size, ArrayList<Item> inventory) {
        super(name, health, damage, groundSpeed, swimmingSpeed, flyingSpeed, size, inventory);
        this.inventory = inventory;
        refreshLoot();
    }
    
    public void addItem(Item item) {
        inventory.add(item);
        item.instance.onEquip();
        item.instance.isEquiped = true;
        refreshLoot();
    }
    
    public void removeItem(int index) {
        inventory.remove(index);
        Item item = getItem(index);
        item.instance.isEquiped = false;
        refreshLoot();
    }
    
    public Item getItem(int index) {
        return inventory.get(index);
    }
    
    public int getIndex(Item item) {
        return inventory.indexOf(item);
    }
    
    public List<Item> getInventory() {
        return inventory;
    }
    
    public void refreshLoot() {
        super.loot = inventory;
    }
    
    public void use() {
        getItem(hand).instance.onUse();
        
    }
}