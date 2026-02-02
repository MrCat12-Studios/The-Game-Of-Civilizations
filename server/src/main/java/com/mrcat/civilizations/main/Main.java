2package com.mrcat.civilizations.main;

import com.mrcat.civilizations.debug.Logging;
import com.mrcat.civilizations.IO.*;
import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    
    Logging logging = Logging.getInstance(); 
    ResourceHandler rh = new ResourceHandler();
    String text = "";

    public static void main(String[] args) {
        Main main = new Main();
    } 

    private void createFiles() {
        File file = new File("data/settings.json");
        if (file.exists()) {
            text = rh.read("data/settings.json");
        }
        else {
            file.mkdir("data");
            rh.newFile("data/settings.json");
        }
    }

    private String selectOption(List<String> options, String question) {
        System.out.println(question);
        int count = 0;
        Scanner scanner = new Scanner(System.in);
        for (String option : options) {
            count++;
            System.out.println(count + ") " + option);
        }
        while (true) {
            try {
                return options.get(scanner.nextInt() - 1);
                break;
            }
            catch (Exception ex) {
                System.out.println("Invalid option. Pick again.");
            }
        }
    }

    private void test() {
        logging.createLog("/sdcard/Download/");
    }
}