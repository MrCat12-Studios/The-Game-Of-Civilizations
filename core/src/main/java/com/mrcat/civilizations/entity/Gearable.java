package com.mrcat.civilizations.entity;

import java.util.List;
import com.mrcat.civilizations.resources.*;

public class Gearable extends Storeable implements Inventoryable {

    public static final String INVENTORY = "inventory";
    public static final String HAND = "hand";
    public static final String OFFHAND = "offhand";
    public static final String HEAD = "head";
    public static final String BODY = "body";
    public static final String LEGS = "legs";
    public static final String[] SLOTS = { INVENTORY, HAND, OFFHAND, HEAD, BODY, LEGS };
    public Tool hand;
    public Tool offhand;
    public Tool head;
    public Tool body;
    public Tool legs;

    public Gearable(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight, int maxWeight) {
        super(name, health, damage, groundSpeed, swimmingSpeed, size, sight, maxWeight);
    }

    public Gearable(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight, int maxWeight, List<Item> inventory) {
        super(name, health, damage, groundSpeed, swimmingSpeed, size, sight, maxWeight, inventory);
    }

    public boolean addItem(Item item, String slot) {
        if (weight + item.weight > maxWeight) return false;
        isUpdated = truec
        switch (slot) {
            case INVENTORY:
                return inventory.add(item);
            case HAND:
                removeItem(getIndex(item));
                hand = (Tool) item;
                break;
            case OFFHAND:
                removeItem(getIndex(item));
                offhand = (Tool) item;
                break;
            case HEAD:
                removeItem(getIndex(item));
                head = (Tool) item;
                break;
            case BODY:
                removeItem(getIndex(item));
                body = (Tool) item;
                break;
            case LEGS:
                removeItem(getIndex(item));
                legs = (Tool) item;
                break;
        }
        refreshLoot();
        return true;
    }

    @Override
    public void refreshLoot() {
        super.loot = inventory;
        loot.add(hand);
        loot.add(offhand);
        loot.add(head);
        loot.add(body);
        loot.add(legs);
        isUpdated = true;
    }

    public void use() {
        
    }
}