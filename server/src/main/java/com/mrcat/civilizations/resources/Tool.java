package com.mrcat.civilizations.resources;

public class Tool extends Item {
    
    public String name;
    public int maxDurability;
    public int durability;
    public String equipSlot;
    
    public Tool(String name, int amount, int weight, int durability, String equipSlot, Events instance) {
        super(name, amount, weight, instance);
        maxDurability = durability;
        this.durability = durability;
        this.equipSlot = equipSlot;
    }
}