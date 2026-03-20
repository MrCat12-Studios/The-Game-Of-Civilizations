package com.mrcat.civilizations.map;

public class World {

    public static String name;
    public static Chunk[][] chunks = new Chunk[180][240];
    
    public World(String name, Chunk[][] chunks) {
        World.name = name;
        World.chunks = chunks;
    }
}