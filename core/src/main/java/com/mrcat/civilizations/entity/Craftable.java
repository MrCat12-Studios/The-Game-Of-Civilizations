package com.mrcat.civilizations.entity;

import com.mrcat.civilizations.resources.Recipe;

public interface Craftable {
    public void craft(Recipe recipe, Building building);
}