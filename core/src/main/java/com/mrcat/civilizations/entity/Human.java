package com.mrcat.civilizations.entity;

import java.util.List;
import java.util.ArrayList;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mrcat.civilizations.map.Position;
import com.mrcat.civilizations.resources.*;

public class Human extends Gearable implements Moveable, Attackable, Craftable, Pickable, Sleepable {

    public boolean isSleeping = false;
    public List<Recipe> recipes = new ArrayList<>();
    public int energy = 100;
    
    public Human(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight, int maxWeight, List<Recipe> recipies) {
        super(name, health, damage, groundSpeed,  swimmingSpeed, size, sight, maxWeight);
        this.recipes = recipes;
    }

    public Human(String name, int health, int damage, int groundSpeed, int swimmingSpeed, int size, int sight, int maxWeight, List<Recipe> recipies, List<Item> inventory) {
        super(name, health, damage, groundSpeed, swimmingSpeed, size, sight, maxWeight, inventory);
        this.recipes = recipes;
    }

    public Human(Gearable g, List<Recipe> recipies) {
        super(g.name, g.health, g.damage, g.groundSpeed, g.swimmingSpeed, g.size, g.sight, g.maxWeight, g.inventory);
        this.recipes = recipes;
    }

    @Override
    public void craft(Recipe recipe, Building building) {
        if (building.recipes.contains(recipe)) {
            Collectable collectable = new Collectable(recipe.product.name, recipe.product);
            collectable.position = position;
        }
    }

    @Override
    public void moveTo(int x, int y) {
        Position newPos = new Position(x, y);
        if (inRange(newPos, 1) && !inRange(newPos, 0) && energy - 25 >= 0) {
            energy -= 25;
            setPosition(x, y);
            isUpdated = true;
        }
    }

    @Override
    public void attack(Entity entity) {
        if (inRange(entity.position, 1) && energy - 40 >= 0) {
            entity.getDamaged(damage + hand.damage);
            isUpdated = true;
        }
    }
    
    @Override
    public boolean pick(Collectable c) {
        isUpdated = true;
        return c.collect(this);
    }

    @Override
    public void sleep() { // TODO
        energy = 100;
        isUpdated = true;
    }

    public void mine(Item i) {
        new Collectable(i.name, i);
    }
    
    public void updateJson() {
        json.attributes.put("maxHealth", new JsonPrimitive(maxHealth));
        json.attributes.put("health", new JsonPrimitive(health));
        json.attributes.put("damage", new JsonPrimitive(damage));
        json.attributes.put("groundSpeed", new JsonPrimitive(groundSpeed));
        json.attributes.put("swimmingSpeed", new JsonPrimitive(swimmingSpeed));
        json.attributes.put("size", new JsonPrimitive(size));
        json.attributes.put("ethnicity", new JsonPrimitive(ethnicity));
        json.attributes.put("sight", new JsonPrimitive(sight));
        JsonArray coords = new JsonArray();
        coords.add(position.x);
        coords.add(position.y);
        json.attributes.put("position", coords);
        json.attributes.put("energy", new JsonPrimitive(energy));
        JsonObject riderObj = new JsonObject();
        for (String i : rider.json.attributes.keySet()) riderObj.add(i, rider.json.attributes.get(i));
        json.attributes.put("rider", riderObj);
        isUpdated = true;
    }
}