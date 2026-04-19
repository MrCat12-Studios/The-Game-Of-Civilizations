package com.mrcat.civilizations;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.mrcat.civilizations.IO.*;
import com.mrcat.civilizations.map.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;

public class Rounds {

    public static final int ROUND = 300000;

    private static Rounds instance = new Rounds();
    public String player;
    public List<String> playerList = new ArrayList<>();
    public int index = 0;
    public Thread timeThread;
    public long start = 0;
    public Json json = new Json("config", "rounds", new HashMap<String, JsonElement>());

    JsonHandler jh = new JsonHandler();

    private Rounds() {}

    public static Rounds getInstance() {
        return instance;
    }

    public void startRounds(boolean jsonExists) {
        if (jsonExists) {
            Json json = jh.parseJson("worlds/" + World.name + "/round.json", false);
            start = jh.getVal("start", json).getAsInt();
            player = jh.getVal("player", json).getAsString();
            JsonArray playersJson = jh.getVal("playerList", json).getAsJsonArray();
            for (JsonElement i : playersJson) playerList.add(i.getAsString());
            int roundsPassed = (int) Math.ceil((System.currentTimeMillis() - start) / 1000) / ROUND;
            index += roundsPassed;
            index = (index + 1) % playerList.size();
        }
        else {
            // in the far far away future
        }
        count();
    }

    public void count() {
        timeThread = new Thread(() -> {
            try {
                while (true) {
                    start = System.currentTimeMillis();
                    index = playerList.indexOf(player);
                    Thread.sleep(ROUND - ((System.currentTimeMillis() - start ) % ROUND));
                    index++;
                    if (index + 1 > playerList.size()) index = 0;
                    player = playerList.get(index);
                }
            }
            catch (Exception ex) {
                // guess what: NOTHING!
            }
        });
    }

    public void save() {
        json.attributes.replace("start", new JsonPrimitive(start));
        json.attributes.replace("player", new JsonPrimitive(player));
        jh.writeJson("worlds/" + World.name + "rounds.json", json);
    }
}