package com.mrcat.civilizations.IO;

import java.lang.Runnable;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.io.IOException;
import java.io.Reader;
import com.google.gson.*;
import com.mrcat.civilizations.debug.Logging;
import com.mrcat.civilizations.entity.*;
import com.mrcat.civilizations.resources.*;

public class JsonHandler {

    Logging logging = Logging.getInstance();
    ResourceHandler rh = new ResourceHandler();
    Gson gson = new Gson();
    Gson gsonBuild = new GsonBuilder().setPrettyPrinting.create();
    
    Runnable onUse = () -> {};
    Runnable onEquip = () -> {};

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
            // to do
        }
        return new Json();
    }
    
    public void writeJson(String path, Json json) {
        
    }

    public Entity generateEntity(Json json) {
        int health = getVal("health", json).getAsInt();
        int damage = getVal("damage", json).getAsInt();
        int gspeed = getVal("ground-speed", json).getAsInt();
        int sspeed = getVal("swimmimg-speed", json).getAsInt();
        int fspeed = getVal("flying-speed", json).getAsInt();
        int size = getVal("chunk-size", json).getAsInt();
        JsonArray items = getVal("loot", json).getAsJsonArray();
        int bdur = getVal("build-duration", json).getAsInt();
        
        Entity entity = new Entity();

        switch (json.type) {
            case "Entity":
                entity = new Entity(json.name, health, damage, gspeed, sspeed, fspeed, size);
                break;
            case "Animal":
                List<Item> loot = new ArrayList<>();
                entity = new Animal(json.name, health, damage, gspeed, sspeed, fspeed, size, loot);
                break;
            case "Human":
                entity = new Human(json.name, health, damage, gspeed, sspeed, fspeed, size);
                break;
            case "Rideable":
                entity = new Rideable(json.name, health, damage, gspeed, sspeed, fspeed, size);
                break;
            case "Building":
                entity = new Building(json.name, health, damage, size, bdur);
                break;
        }
        return entity;
    }

    public Item generateResource(Json json) {
        int amount = getVal("amount", json).getAsInt();
        int weight = getVal("weight", json).getAsInt();
        int durab = getVal("durability", json).getAsInt();
        String equipSlot = getVal("equip-slot", json).getAsString();
        JsonObject events = getVal("events", json).getAsJsonObject();
        
        boolean isEquiped = false;
        String onEquipTxt = "";
        String onUseTxt = "";
        for (String key : events.keySet()) {
            switch (key) {
                case "is-equiped":
                    if (events.get(key).getAsBoolean()) isEquiped = true;
                    break;
                case "on-equip":
                    switch (events.get(key).getAsString().substring(0, events.get(key).toString().length() - 3)) {
                        case "protection":
                            onEquipTxt = "protection";
                            onEquip = () -> { }; // to do
                            break;
                        case "damage":
                            onEquipTxt = "damage";
                            onEquip = () -> { }; // to do
                            break;
                    }
                    break;
            }
        }
        Events eventsObj = new Events() {
            public void onEquip() {
                onEquip.run();
            }
            
            public void onUse() {
                onUse.run();
            }
        };
        eventsObj.isEquiped = isEquiped;
        eventsObj.onEquip = onEquipTxt;
        eventsObj.onUse = onUseTxt;
        Item item = new Item();
        switch (json.type) {
            case "Item":
                item = new Item(json.name, amount, weight, eventsObj);
                break;
            case "Tool":
                item = new Tool(json.name, amount, weight, durab, equipSlot, eventsObj);
                break;
        }
        return item;
    }

    public JsonElement getVal(String key, Json json) {
        if (json.attributes.containsKey(key)) return json.attributes.get(key);
        return null;
    }
    
    public class Json {

        String type;
        String name;
        Map<String, JsonElement> attributes;

        public Json(String type, String name, Map<String, JsonElement> attributes) {
            this.type = type;
            this.name = name;
            this.attributes = attributes;
        }
    }
}