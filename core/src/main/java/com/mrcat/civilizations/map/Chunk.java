package com.mrcat.civilizations.map;

import java.util.List;
import java.util.ArrayList;
import com.mrcat.civilizations.entity.Entity;

public class Chunk {

    public List<Entity> units = new ArrayList<>();
    String terrainType;
    int chunkCapacity;
    int usedChunkCapacity;
    
    public Chunk(String biome) {
        terrainType = biome;
    }

    public String getBiome() {
        return terrainType;
    }

    public boolean canFit(int size) {
        return usedChunkCapacity + size <= chunkCapacity;
    }
}