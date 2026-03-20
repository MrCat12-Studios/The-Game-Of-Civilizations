package com.mrcat.civilizations;

import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.InputMismatchException;
import com.mrcat.civilizations.debug.Logging;
import com.mrcat.civilizations.IO.*;
import com.mrcat.civilizations.entity.Entity;
import com.mrcat.civilizations.map.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;

public class Main {

    Scanner scanner = new Scanner(System.in);
    Logging logging = Logging.getInstance(); 
    ResourceHandler rh = new ResourceHandler();
    JsonHandler jh = new JsonHandler();
    String text = "";
    World world;

    public static void main(String[] args) {
        Main main = new Main();
        main.createFiles();
        main.selectWorld();
    } 

    private void createFiles() {
        File file = new File("settings.json");
        if (file.exists()) {
            text = rh.read("settings.json");
        }
        else {
            rh.newFile("settings.json");
            new File("worlds").mkdir();
        }
        logging.createLog("/sdcard/Download"); // change this on release
    }

    private String selectOption(List<String> options, String question) {
        System.out.println(question);
        int count = 0;
        for (String option : options) {
            count++;
            System.out.println("  " + count + ") " + option);
        }
        while (true) {
            try {
                return options.get(scanner.nextInt() - 1);
            }
            catch (InputMismatchException | IndexOutOfBoundsException ex) {
                System.out.println("Invalid option. Pick again.\n");
                scanner.nextLine();
            }
        }
    }

    private void selectWorld() {
        List<String> options = new ArrayList<>();
        options.add("New world");
        options.add("Play existing");
        String selOption = selectOption(options, "Select option:\n");
        scanner.nextLine();
        String worldName;
        if (selOption == "New world") {
            System.out.println("Input world name. Keep in mind that spaces and slashes (/) not allowed:\n");
            worldName = scanner.nextLine();
            System.out.println("Input seed (Enter \"0\" for a random seed):\n");
            double seed = scanner.nextDouble();
            scanner.nextLine();
            world = generateWorld(worldName, seed);
        }
        else {
            options = new ArrayList<>();
            for (String path : new File("worlds").list()) {
                if (new File("worlds/" + path).isDirectory()) {
                    options.add(new File("worlds/" + path).getName());
                }
            }
            worldName = selectOption(options, "Select world:\n");
            Json json = jh.parseJson("worlds/" + worldName + "/world.json", false);
            JsonArray rows = json.attributes.get("map").getAsJsonArray();
            Chunk[][] chunks = new Chunk[180][240];
            for (int y = 0; y < 180; y++) {
                JsonArray row = rows.get(y).getAsJsonArray();
                for (int x = 0; x < 240; x++) {
                    chunks[y][x] = new Chunk(row.get(x).getAsString());
                }
            }
            world = new World(worldName, chunks);
        }
        File dir = new File("worlds/" + worldName + "/entities");
        if (dir.exists()) {
            if (dir.listFiles().length > 0) for (File i : dir.listFiles()) {
                Json json = jh.parseJson(i, false);
                jh.generateEntity(json);
            }
        }
        else {
            dir.mkdir();
        }
        GameServer server = new GameServer();
        server.startServer();
    }

    private World generateWorld(String worldName, double seed) {
        new File("worlds/" + worldName).mkdir();
        NoiseGenerator noiseGen = new NoiseGenerator(seed);
        double[][] altitude = new double[180][240];
        double[][] temperature = new double[180][240];
        double[][] humidity = new double[180][240];
        for (int y = 0; y < 180; y++) {
            for (int x = 0; x < 240; x++) {
                altitude[y][x] = (double) Math.round(noiseGen.noise(x, y) * 100) / 100;
                temperature[y][x] = (double) Math.round(noiseGen.noise(x + 1000, y + 1000) * 100) / 100;
                humidity[y][x] = (double) Math.round(noiseGen.noise(x - 1000, y - 1000) * 100) / 100;
            }
        }
        Chunk[][] chunks = new Chunk[180][240];
        Map<String, JsonElement> attributes = new HashMap<>();
        JsonArray row = new JsonArray();
        JsonArray rows = new JsonArray();
        String biome;
        for (int y = 0; y < 180; y++) {
            for (int x = 0; x < 240; x++) {
                if (altitude[y][x] > 0.6) { // mountain
                    if (temperature[y][x] > 0.6) biome = "badlands-plateu";
                    else if (temperature[y][x] > 0) biome = "mountain";
                    else if (temperature[y][x] > -0.5) biome = "snowy-mountain";
                    else biome = "frozen-peaks";
                }
                else if (altitude[y][x] > 0.4) { // hills
                    if (temperature[y][x] > 0.6) biome = "badlands-mesa";
                    else if (temperature[y][x] > 0) biome = "hills";
                    else if (temperature[y][x] > -0.5) biome = "snowy-hills";
                    else biome = "frozen-hills";
                }
                else if (altitude[y][x] > 0.1) { // woodland
                    if (temperature[y][x] > 0.6) {
                        if (humidity[y][x] < 0) biome = "shrublands";
                        else biome = "scorched-forest";
                    }
                    else if (temperature[y][x] > 0.4) {
                        if (humidity[y][x] < 0) biome = "savanna";
                        else biome = "rainforest";
                    }
                    else if (temperature[y][x] > 0) {
                        if (humidity[y][x] < 0) biome = "woodland";
                        else biome = "swamp";
                    }
                    else biome = "taiga";
                }
                else if (altitude[y][x] > -0.5) { // grassland
                    if (temperature[y][x] > 0.6) {
                        if (humidity[y][x] < 0) biome = "desert";
                        else biome = "steppe";
                    }
                    else if (temperature[y][x] > 0) {
                        if (humidity[y][x] < 0) biome = "grassland";
                        else biome = "marsh";
                    }
                    else if (temperature[y][x] > -0.5) biome = "meadow";
                    else biome = "tundra";
                }
                else if (altitude[y][x] > -0.4) { // coast
                    if (temperature[y][x] > 0.4) biome = "beach";
                    else if (temperature[y][x] > 0.4) biome = "gravely-shore";
                    else biome = "stony-shore";
                }
                else { // waters
                    biome = "waters";
                }
                row.add(biome);
                chunks[y][x] = new Chunk(biome);
            }
            rows.add(row);
            row = new JsonArray();
            attributes.put("map", rows);
        }
        Json json = new Json("world", worldName, attributes);
        String path = "worlds/" + worldName + "/";
        rh.newFile(path + "world.json");
        jh.writeJson(path + "world.json", json);
        return new World(worldName, chunks);
    }

    private void setPlayers() {
        
    }
}