package com.mrcat.civilizations.map;

public class Region {

    Chunk[][] chunks = new Chunk[5][5];
    private String biome;
    private Position position;
    
    public Region(Position coords, String biome) {
        this.biome = biome;
        position = coords;
    }
}