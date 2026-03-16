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

    public void writeJson(String path, Json json) {
        rh.write(path, gsonBuilder.toJson(json), false);
    }

    public void writeJson(File file, Json json) {
        writeJson(file.getPath(), json);
    }

    public Entity generateEntity(Json json) {
        String id = getVal("id", json).getAsString();
        Integer maxHealth = getVal("max-health", json).getAsInt();
        Integer health = getVal("health", json).getAsInt();
        Integer damage = getVal("damage", json).getAsInt();
        Integer gspeed = getVal("ground-speed", json).getAsInt();
        Integer sspeed = getVal("swimmimg-speed", json).getAsInt();
        Integer size = getVal("chunk-size", json).getAsInt();
        Integer sight = getVal("sight-range", json).getAsInt();
        Integer maxWeight = getVal("max-weight", json).getAsInt();
        Integer bdur = getVal("build-duration", json).getAsInt();
        JsonArray coords = getVal("position", json).getAsJsonArray();
        Position position = (coords != null) ? new Position(coords.get(0).getAsInt(), coords.get(1).getAsInt()) : null;
        JsonArray lootJson = getVal("loot", json).getAsJsonArray();
        JsonArray inventoryJson = getVal("inventory", json).getAsJsonArray();
        JsonObject handJson = getVal("main-hand", json).getAsJsonObject();
        JsonObject offhandJson = getVal("off-hand", json).getAsJsonObject();
        JsonObject headJson = getVal("head", json).getAsJsonObject();
        JsonObject bodyJson = getVal("body", json).getAsJsonObject();
        JsonObject legsJson = getVal("legs", json).getAsJsonObject();
        List<Item> loot = new ArrayList<>();
        List<Item> inventory = new ArrayList<>();
        Tool hand = null;
        Tool offhand = null;
        Tool head = null;
        Tool body = null;
        Tool legs = null;
        try {
            if (lootJson != null) for (JsonElement i : lootJson) inventory.add(generateResource(gson.fromJson(i, Json.class)));
            if (inventoryJson != null) for (JsonElement i : inventoryJson) inventory.add(generateResource(gson.fromJson(i, Json.class)));
            if (handJson != null) hand = (Tool) generateResource(gson.fromJson(handJson, Json.class));
            if (offhandJson != null) offhand = (Tool) generateResource(gson.fromJson(offhandJson, Json.class));
            if (headJson != null) head = (Tool) generateResource(gson.fromJson(headJson, Json.class));
            if (bodyJson != null) body = (Tool) generateResource(gson.fromJson(bodyJson, Json.class));
            if (legsJson != null) legs = (Tool) generateResource(gson.fromJson(legsJson, Json.class));
        }
        catch (Exception ex) { // TODO
            logging.addLog(ex.getMessage(), logging.logExists());
            return null;
        }
        Entity entity;
        if (maxHealth == null) {
            Json json2 = parseJson("entities/" + json.name + ".json", true);
            entity = generateEntity(json2);
            entity.health = health;
            if (position != null) entity.position = position;
            Human human;
            if (json.type == "Human") {
                human = (Human) entity;
                if (inventory != null) human = new Human(json.name, maxHealth, damage, gspeed, sspeed, size, sight, maxWeight, inventory);
                if (hand != null) human.addItem(hand, Human.Slot.HAND);
                if (offhand != null) human.addItem(offhand, Human.Slot.OFFHAND);
                if (head != null) human.addItem(head, Human.Slot.HEAD);
                if (body != null) human.addItem(body, Human.Slot.BODY);
                if (legs != null) human.addItem(legs, Human.Slot.LEGS);
                return entity;
            }
        }

        switch (json.type) {
            case "Entity":
                entity = new Entity(json.name, maxHealth, damage, gspeed, sspeed, size, sight);
                break;
            case "Animal":
                entity = new Animal(json.name, maxHealth, damage, gspeed, sspeed, size, sight, loot);
                break;
            case "Human":
                Human human = new Human(json.name, health, damage, gspeed, sspeed, size, sight, maxWeight);
                if (inventory != null) human = new Human(json.name, health, damage, gspeed, sspeed, size, sight, maxWeight, inventory);
                if (hand != null) human.addItem(hand, Human.Slot.HAND);
                if (offhand != null) human.addItem(offhand, Human.Slot.OFFHAND);
                if (head != null) human.addItem(head, Human.Slot.HEAD);
                if (body != null) human.addItem(body, Human.Slot.BODY);
                if (legs != null) human.addItem(legs, Human.Slot.LEGS);
                entity = human;
                break;
            case "Rideable":
                entity = new Rideable(json.name, maxHealth, damage, gspeed, sspeed, size, sight);
                break;
            default: // case Building
                entity = new Building(json.name, maxHealth, damage, size, sight, bdur);
                break;
        }
        if (position != null) entity.position = position;
        return entity;
    }

    public Item generateResource(Json json) {
        String name = getVal("name", json).getAsString();
        Integer weight = getVal("weight", json).getAsInt();
        Integer maxDurability = getVal("max-durability", json).getAsInt();
        Integer durability = getVal("current-durability", json).getAsInt();
        String equipSlot = getVal("equip-slot", json).getAsString();
        Integer protection = getVal("protection", json).getAsInt();
        Integer damage = getVal("damage", json).getAsInt();
        Integer range = getVal("range", json).getAsInt();
        Map<String, Integer> miningRates = new HashMap<>();
        List<String> consumes = new ArrayList<>();
        JsonObject ratesObj = getVal("mining-rates", json).getAsJsonObject();
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
                item = new Tool(json.name, weight, durability, range, equipSlot);
                break;
        }
        return item;
    }

    public JsonElement getVal(String key, Json json) {
        if (json.attributes.containsKey(key)) return json.attributes.get(key);
        return null;
    }
}