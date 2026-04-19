package com.mrcat.civilizations.entity;

import java.util.List;
import com.mrcat.civilizations.resources.Item;

public interface Inventoryable {
    public boolean addItem(Item item);

    public void refreshLoot();

    public List<Item> getInventory();

    public int getIndex(Item item);

    public Item getItem(int index);

    public void removeItem(int index);
}