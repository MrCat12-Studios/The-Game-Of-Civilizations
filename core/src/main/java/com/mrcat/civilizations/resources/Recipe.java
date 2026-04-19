package com.mrcat.civilizations.resources;

import java.util.List;
import java.util.ArrayList;

public class Recipe {

    public String name;
    public List<Item> materials = new ArrayList<>();
    public Item product;
    public int craftingDuration;

    public Recipe(String name, int craftingDuration, List<Item> materials, Item product) {
        this.name = name;
        this.materials = materials;
        this.product = product;
    }
}