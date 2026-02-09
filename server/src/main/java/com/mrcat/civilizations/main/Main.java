package com.mrcat.civilizations.main;

import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
import com.mrcat.civilizations.debug.Logging;
import com.mrcat.civilizations.IO.*;
import com.mrcat.civilizations.map.*;

public class Main {
    
    Logging logging = Logging.getInstance(); 
    ResourceHandler rh = new ResourceHandler();
    String text = "";

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
    }

    private String selectOption(List<String> options, String question) {
        System.out.println(question);
        int count = 0;
        Scanner scanner = new Scanner(System.in);
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
        if (selOption == "New world") {
            generateWorld();
        }
        else {
            options = new ArrayList<>();
            for (String path : new File("worlds").list()) {
                if (new File(path).isDirectory()) options.add(new File(path).getName());
            }
            String selWorld = selectOption(options, "Select world:\n");
        }
    }

    public void generateWorld() {
        System.out.println("Input world name. Keep in mind that spaces and slashes (/) not allowed:\n");
        Scanner scanner = new Scanner(System.in);
        new File("worlds/" + scanner.nextLine()).mkdir();
        System.out.println("Input seed (Enter \"0\" for a random seed):\n");
        double seed = scanner.nextDouble();
        scanner.nextLine();
        NoiseGenerator noiseGen = new NoiseGenerator(seed);
        double[][] altitude = new double[180][240];
        for (int y = 0; y < 180; y++) {
            for (int x = 0; x < 240; x++) {
                altitude[y][x] = (double) Math.round(noiseGen.noise(x, y) * 100) / 100;
                rh.write("test.txt", altitude[y][x] + " ", true);
            }
            rh.write("test.txt", "\n", true);
        }
        double[][] temperature = new double[180][240];
        for (int y = 0; y < 180; y++) {
            for (int x = 0; x < 240; x++) {
                temperature[y][x] = (double) Math.round(noiseGen.noise(x + 1000, y + 1000) * 100) / 100;
            }
        }
        double[][] humidity = new double[180][240];
        for (int y = 0; y < 180; y++) {
            for (int x = 0; x < 240; x++) {
                humidity[y][x] = (double) Math.round(noiseGen.noise(x - 1000, y - 1000) * 100) / 100;
            }
        }
        Chunk[][] chunks = new Chunk[180][240];
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
                chunks[y][x] = new Chunk(biome);
            }
        }
    }
}