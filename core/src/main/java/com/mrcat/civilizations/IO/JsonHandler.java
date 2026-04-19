package com.mrcat.civilizations.IO;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.Reader;
import com.google.gson.*;
import com.mrcat.civilizations.debug.Logging;
import com.mrcat.civilizations.entity.*;
import com.mrcat.civilizations.resources.*;
import com.mrcat.civilizations.map.*;

public class JsonHandler {

    Logging logging = Logging.getInstance();
    ResourceHandler rh = new ResourceHandler();
    Gson gson = new Gson();
    Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

    public Json parseJson(String path, boolean fromJar) {
        try {
            if (fromJar) {
                Json json = gson.fromJson(rh.convertBytes(rh.read(path, true)), Json.class);
                return json;
            }
            else {
                Reader r = rh.getReader(path);
                Json json = gson.fromJson(r, Json.class);
                return json;
            }
        }
        catch (IOException ex) {
            // TODO
        }
        return null;
    }

    public Json parseJson(File file, boolean fromJar) {
        return parseJson(file.getPath(), fromJar);
    }

    public Json parseJson(String json) {
        return gson.fromJson(json, Json.class);
    }

    public void writeJson(String path, Json json) {
        rh.write(path, buildJson(json), false);
    }

    public void writeJson(File file, Json json) {
        writeJson(file.getPath(), json);
    }

    public void writeJson(String path, String json) {
        rh.write(path, json, false);
    }

    public void writeJson(File file, String json) {
        writeJson(file.getPath(), json);
    }

    public String buildJson(Json json) {
        return gsonBuilder.toJson(json);
    }

    public Entity generateEntity(Json json) {
        String id = (getVal("id", json) != null && !getVal("id", json).isJsonNull()) ? getVal("id", json).getAsString() : null;
        Integer maxHealth = (getVal("maxHealth", json) != null && !getVal("maxHealth", json).isJsonNull()) ? getVal("maxHealth", json).getAsInt() : null;
        Integer health = (getVal("health", json) != null && !getVal("health", json).isJsonNull()) ? getVal("health", json).getAsInt() : null;
        Integer damage = (getVal("damage", json) != null && !getVal("damage", json).isJsonNull()) ? getVal("damage", json).getAsInt() : null;
        Integer gspeed = (getVal("groundSpeed", json) != null && !getVal("groundSpeed", json).isJsonNull()) ? getVal("groundSpeed", json).getAsInt() : null;
        Integer sspeed = (getVal("swimmimgspeed", json) != null && !getVal("swimmimgspeed", json).isJsonNull()) ? getVal("swimmimgspeed", json).getAsInt() : null;
        Integer size = (getVal("chunkSize", json) != null && !getVal("chunkSize", json).isJsonNull()) ? getVal("chunkSize", json).getAsInt() : null;
        Integer sight = (getVal("sightRange", json) != null && !getVal("sightRange", json).isJsonNull()) ? getVal("sightRange", json).getAsInt() : null;
        Integer maxWeight = (getVal("maxWeight", json) != null && !getVal("maxWeight", json).isJsonNull()) ? getVal("maxWeight", json).getAsInt() : null;
        Integer weight = (getVal("weight", json) != null && !getVal("weight", json).isJsonNull()) ? getVal("weight", json).getAsInt() : null;
        Integer bdur = (getVal("buildDuration", json) != null && !getVal("buildDuration", json).isJsonNull()) ? getVal("buildDuration", json).getAsInt() : null;
        JsonArray coords = (getVal("position", json) != null && !getVal("position", json).isJsonNull()) ? getVal("position", json).getAsJsonArray() : null;
        Position position = (coords != null) ? new Position(coords.get(0).getAsInt(), coords.get(1).getAsInt()) : null;
        String ethnicity = (getVal("ethnicity", json) != null && !getVal("ethnicity", json).isJsonNull()) ? getVal("ethnicity", json).getAsString() : null;
        Integer energy = (getVal("energy", json) != null && !getVal("energy", json).isJsonNull()) ? getVal("energy", json).getAsInt() : null;
        JsonArray lootJson = (getVal("loot", json) != null && !getVal("loot", json).isJsonNull()) ? getVal("loot", json).getAsJsonArray() : null;
        JsonArray inventoryJson = (getVal("inventory", json) != null && !getVal("inventory", json).isJsonNull()) ? getVal("inventory", json).getAsJsonArray() : null;
        JsonObject handJson = (getVal("mainHand", json) != null && !getVal("mainHand", json).isJsonNull()) ? getVal("mainHand", json).getAsJsonObject() : null;
        JsonObject offhandJson = (getVal("offHand", json) != null && !getVal("offHand", json).isJsonNull()) ? getVal("offHand", json).getAsJsonObject() : null;
        JsonObject headJson = (getVal("head", json) != null && !getVal("head", json).isJsonNull()) ? getVal("head", json).getAsJsonObject() : null;
        JsonObject bodyJson = (getVal("body", json) != null && !getVal("body", json).isJsonNull()) ? getVal("body", json).getAsJsonObject() : null;
        JsonObject legsJson = (getVal("legs", json) != null && !getVal("legs", json).isJsonNull()) ? getVal("legs", json).getAsJsonObject() : null;
        JsonObject riderJson = (getVal("rider", json) != null && !getVal("rider", json).isJsonNull()) ? getVal("rider", json).getAsJsonObject() : null;
        JsonArray recipesJson = (getVal("recipes", json) != null && !getVal("recpies", json).isJsonNull()) ? getVal("recipes", json).getAsJsonArray() : null;
        Entity rider = generateEntity(gson.fromJson(riderJson, Json.class));
        List<Item> loot = new ArrayList<>();
        List<Item> inventory = new ArrayList<>();
        Tool hand = null;
        Tool offhand = null;
        Tool head = null;
        Tool body = null;
        Tool legs = null;
        List<Recipe> recipes = new ArrayList<>();
        try {
            if (lootJson != null) for (JsonElement i : lootJson) loot.add(generateResource(gson.fromJson(i, Json.class)));
            if (inventoryJson != null) for (JsonElement i : inventoryJson) inventory.add(generateResource(gson.fromJson(i, Json.class)));
            if (handJson != null) hand = (Tool) generateResource(gson.fromJson(handJson, Json.class));
            if (offhandJson != null) offhand = (Tool) generateResource(gson.fromJson(offhandJson, Json.class));
            if (headJson != null) head = (Tool) generateResource(gson.fromJson(headJson, Json.class));
            if (bodyJson != null) body = (Tool) generateResource(gson.fromJson(bodyJson, Json.class));
            if (legsJson != null) legs = (Tool) generateResource(gson.fromJson(legsJson, Json.class));
            if (recipesJson != null) for (JsonElement i : recipesJson) recipes.add(generateRecipe(parseJson("recipes/" + i + ".json", true)));
        }
        catch (Exception ex) { // TODO
            logging.addLog(ex.getMessage(), logging.logExists());
            return null;
        }
        Entity entity = Entity.DEFAULT;
        if (maxHealth == null) {
            Json json2 = parseJson("entities/" + json.name + ".json", true);
            json.attributes.putAll(json2.attributes);
            entity = generateEntity(json);
        }
        boolean isHuman = false;
        switch (json.type) {
            case "Lootable":
                entity = new Lootable(json.name, maxHealth, damage, gspeed, sspeed, size, sight, loot);
                break;
            case "Storeable":
                entity = new Storeable(json.name, maxHealth, damage, gspeed, sspeed, size, sight, maxWeight, inventory);
                break;
            case "Human":
                isHuman = true;
            case "Gearable":
                Gearable gearable = new Gearable(json.name, health, damage, gspeed, sspeed, size, sight, maxWeight, inventory);
                if (hand != null) gearable.addItem(hand, Gearable.HAND);
                if (offhand != null) gearable.addItem(offhand, Gearable.OFFHAND);
                if (head != null) gearable.addItem(head, Gearable.HEAD);
                if (body != null) gearable.addItem(body, Gearable.BODY);
                if (legs != null) gearable.addItem(legs, Gearable.LEGS);
                Human human;
                if (isHuman) {
                    human = new Human(gearable, recipes);
                    if (energy != null) human.energy = energy;
                }
                entity = gearable;
                break;
            case "Building":
                Building building = new Building(json.name, maxHealth, damage, size, sight, maxWeight, bdur);
                building.recipes = recipes;
                break;
            case "Entity":
                entity = new Entity(json.name, maxHealth, damage, gspeed, sspeed, size, sight);
                break;
        }
        if (position != null) entity.position = position;
        entity.json = json;
        return entity;
    }

    public Item generateResource(Json json) {
        String name = getVal("name", json).getAsString();
        Integer weight = getVal("weight", json).getAsInt();
        Integer maxDurability = getVal("maxDurability", json).getAsInt();
        Integer durability = getVal("currentDurability", json).getAsInt();
        String equipSlot = getVal("equipSlot", json).getAsString();
        Integer protection = getVal("protection", json).getAsInt();
        Integer damage = getVal("damage", json).getAsInt();
        Integer range = getVal("range", json).getAsInt();
        Map<String, Integer> miningRates = new HashMap<>();
        List<String> consumes = new ArrayList<>();
        JsonObject ratesObj = getVal("miningRates", json).getAsJsonObject();
        for (String key : ratesObj.keySet()) {
            miningRates.put(key, ratesObj.get(key).getAsInt());
        }
        for (JsonElement i : getVal("consumes", json).getAsJsonArray()) consumes.add(i.getAsString());

        if (weight == null) {
            Json json2 = parseJson("items/" + name + ".json", true);
            Item item = generateResource(json2);
            if (durability != null) {
                Tool tool = (Tool) item;
                tool.durability = durability;
                if (protection != null) tool.protection = protection;
                if (damage != null) tool.damage = damage;
                if (miningRates.size() != 0) tool.miningRates = miningRates;
                if (consumes.size() != 0) tool.consumes = consumes;
                return tool;
            }
            return item;
        }
        Item item = new Item();
        switch (json.type) {
            case "Item":
                item = new Item(json.name, weight);
                break;
            case "Tool":
                Tool tool = new Tool(json.name, weight, durability, range, equipSlot);
                tool.miningRates = miningRates;
                tool.consumes = consumes;
                tool.damage = damage;
                tool.protection = protection;
                break;
        }
        return item;
    }

    public Recipe generateRecipe(Json json) {
        String name = json.name;
        Integer craftDur = getVal("product", json).getAsInt();
        String productJson = getVal("product", json).getAsString();
        JsonArray materialsJson = getVal("materials", json).getAsJsonArray();
        Item product = generateResource(parseJson("items/" + productJson + ".json", true));
        List<Item> materials = new ArrayList<>();
        for (JsonElement i : materialsJson) materials.add(generateResource(parseJson("items/" + i.getAsString() + ".json", true)));
        return new Recipe(name, craftDur, materials, product);
    }

    public JsonElement getVal(String key, Json json) {
        if (json.attributes.containsKey(key)) return json.attributes.get(key);
        return null;
    }
}