package com.mrcat.civilizations.entity;

import java.util.List;
import java.util.ArrayList;
import com.mrcat.civilizations.resources.*;

public class Storeable extends Lootable implements Inventoryable {

    protected List<Item> inventory = new ArrayList<>();
    protected int maxWeight;
    protected int weight;

    public Storeable(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight, int maxWeight) {
        super(name, health, damage, groundSpeed, swimmingSpeed, size, sight, new ArrayList<>());
        this.maxWeight = maxWeight;
        weight = maxWeight;
    }

    public Storeable(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight, int maxWeight, List<Item> inventory) {
        super(name, health, damage, groundSpeed, swimmingSpeed, size, sight, inventory);
        int weight = 0;
        for (Item i : inventory) weight += i.weight;
        if (weight <= maxWeight) this.weight = weight;
    }

    @Override
    public boolean addItem(Item item) {
        if (weight + item.weight > maxWeight) return false;
        inventory.add(item);
        weight += item.weight;
        refreshLoot();
        return true;
    }

    @Override
    public void removeItem(int index) {
        Item item = getItem(index);
        inventory.remove(index);
        weight -= item.weight;
        refreshLoot();
    }

    @Override
    public Item getItem(int index) {
        return inventory.get(index);
    }

    @Override
    public int getIndex(Item item) {
        return inventory.indexOf(item);
    }

    @Override
    public List<Item> getInventory() {
        return inventory;
    }

    @Override
    public void refreshLoot() {
        super.loot = inventory;
    }
}