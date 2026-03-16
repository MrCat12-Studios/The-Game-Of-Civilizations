package com.mrcat.civilizations.IO;

import java.util.Map;
import com.google.gson.JsonElement;
    
public class Json {

    public String type;
    public String name;
    public Map<String, JsonElement> attributes;

    public Json(String type, String name, Map<String, JsonElement> attributes) {
        this.type = type;
        this.name = name;
        this.attributes = attributes;
    }
}