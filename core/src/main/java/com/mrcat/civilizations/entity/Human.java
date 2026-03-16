package com.mrcat.civilizations.entity;

import java.util.List;
import java.util.ArrayList;
import com.mrcat.civilizations.resources.*;

public class Human extends Animal {
    
    public enum Slot {
        INVENTORY,
        HAND,
        OFFHAND,
        HEAD,
        BODY,
        LEGS
    }

    protected List<Item> inventory = new ArrayList<>();
    protected int maxWeight;
    protected int weight;
    public Tool hand;
    public Tool offhand;
    public Tool head;
    public Tool body;
    public Tool legs;

    public Human(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight, int maxWeight) {
        super(name, health, damage, groundSpeed, swimmingSpeed, size, sight, new ArrayList<>());
    }

    public Human(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight, int maxWeight, List<Item> inventory) {
        super(name, health, damage, groundSpeed, swimmingSpeed, size, sight, inventory);
        int weight = 0;
        for (Item i : inventory) weight += i.weight;
        if (weight <= maxWeight) this.weight = weight;
    }

    public void addItem(Item item, Slot slot) {
        if (weight + item.weight > maxWeight) return;
        switch (slot) {
            case Slot.INVENTORY:
                inventory.add(item);
                break;
            case HAND:
                removeItem(getIndex(item));
                addItem(hand, Slot.INVENTORY);
                hand = (Tool) item;
                break;
            case OFFHAND:
                removeItem(getIndex(item));
                addItem(offhand, Slot.INVENTORY);
                offhand = (Tool) item;
                break;
            case HEAD:
                removeItem(getIndex(item));
                addItem(head, Slot.INVENTORY);
                head = (Tool) item;
                break;
            case BODY:
                removeItem(getIndex(item));
                addItem(body, Slot.INVENTORY);
                body = (Tool) item;
                break;
            case LEGS:
                removeItem(getIndex(item));
                addItem(legs, Slot.INVENTORY);
                legs = (Tool) item;
                break;
        }
        refreshLoot();
    }

    public void removeItem(int index) {
        inventory.remove(index);
        Item item = getItem(index);
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
        loot.add(hand);
        loot.add(offhand);
        loot.add(head);
        loot.add(body);
        loot.add(legs);
    }

    public void use() {
        
    }
}