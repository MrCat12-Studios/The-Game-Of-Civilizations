package com.mrcat.civilizations.entity;

import com.mrcat.civilizations.resources.Item;

public class Collectable extends Entity {

    Item loot;

    public Collectable(String name, Item loot) {
        super(name, 1, 0, 0, 0, 0, 0);
    }

    public boolean collect(Inventoryable i) {
        boolean added = i.addItem(loot);
        if (added) destroy();
        isUpdated = true;
        return added;
    }
}