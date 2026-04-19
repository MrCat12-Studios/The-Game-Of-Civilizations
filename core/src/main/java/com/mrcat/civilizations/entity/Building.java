package com.mrcat.civilizations.entity;

import java.util.List;
import java.util.ArrayList;
import com.mrcat.civilizations.resources.Recipe;

public class Building extends Storeable {

    public int buildDuration;
    public List<Recipe> recipes = new ArrayList<>();

    public Building(String name, int health, int damage, int size, int sight, int maxWeight, int buildDuration) {
        super(name, health, damage, 0, 0, size, sight, maxWeight);
        this.buildDuration = buildDuration;
    }
}