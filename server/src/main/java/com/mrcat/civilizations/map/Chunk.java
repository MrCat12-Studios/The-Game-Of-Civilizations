package com.mrcat.civilizations.map;

import java.util.List;
import java.util.ArrayList;
import com.mrcat.civilizations.entity.Entity;

public class Chunk {
    
    public List<Entity> units = new ArrayList<>();
    public String terrainType;
    
    public Chunk(String biome) {
        terrainType = biome;
    }
    
}