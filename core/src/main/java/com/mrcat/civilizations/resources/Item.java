package com.mrcat.civilizations.resources;

public class Item {
    
    public String name;
    public int weight;
    
    public Item(String name, int weight) {
        this.name = name;
        this.weight = weight;

    }
    
    public Item() {}
    
    public String getName() {
        return name;
    }
}