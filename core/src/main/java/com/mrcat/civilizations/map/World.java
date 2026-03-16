package com.mrcat.civilizations.map;

public class World {
    
    public Chunk[][] chunks = new Chunk[180][240];
    
    public World(Chunk[][] chunks) {
        this.chunks = chunks;
    }
}