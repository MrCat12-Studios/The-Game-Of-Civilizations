package com.mrcat.civilizations.map;

public class Position {
    
    public int x;
    public int y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Position(String pos) {
        this(Integer.parseInt(pos.split("\\.")[0]), Integer.parseInt(pos.split("\\.")[1]));
    }
}