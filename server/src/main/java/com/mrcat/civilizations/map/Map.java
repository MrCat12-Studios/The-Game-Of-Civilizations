package com.mrcat.civilizations.map;

public class Map {
    
    Region[][] regions = new Region[36][48];
    Chunk[][] chunks = new Chunk[180][240];
    
    public Map(Chunk[][] chunks) {
        this.chunks = chunks;
    }
}