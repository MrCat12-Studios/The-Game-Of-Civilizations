package com.mrcat.civilizations.resources;

import java.util.List;
import java.util.ArrayList;

public class Item {
    
    private String name;
    public int amount;
    public int weight;
    public Events instance;
    
    public Item(String name, int amount, int weight, Events instance) {
        this.name = name;
        this.amount = amount;
        this.weight = weight;
        this.instance = instance;
    }
    
    public Item() {}
    
    public String getName() {
        return name;
    }
}