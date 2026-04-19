package com.mrcat.civilizations.resources;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Tool extends Item {

    public int maxDurability;
    public int durability;
    public String equipSlot;
    public int protection = 0;
    public int damage = 0;
    public int range;
    public Map<String, Integer> miningRates = new HashMap<>();
    public List<String> consumes = new ArrayList<>();
    
    public Tool(String name, int weight, int durability, int range, String equipSlot) {
        super(name, weight);
        maxDurability = durability;
        this.durability = durability;
        this.equipSlot = equipSlot;
    }
}