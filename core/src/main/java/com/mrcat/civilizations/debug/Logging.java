package com.mrcat.civilizations.debug;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import com.mrcat.civilizations.lifecycle.Clock;
import com.mrcat.civilizations.IO.ResourceHandler;

public class Logging {
    
    private static Logging instance;
    Clock clock = new Clock();
    private File log;
    public List<String> logRegistry = new ArrayList<String>();
    ResourceHandler rh;
    String path;
    
    private Logging() {}
    
    public static Logging getInstance() {
        if (instance == null) instance = new Logging();
        return instance;
    }
    
    public void createLog(String path) {
        rh = new ResourceHandler();
        this.path = path + "log.txt";
        log = rh.newFile(this.path);
    }
    
    public void addLog(String message, boolean writeable) {
        message = formatLog(message);
        logRegistry.add(message);
        System.out.println(message);
        if (writeable) rh.write(path, message + "\n", true);
    }
    
    public String formatLog(String message) {
        return "[" +  clock.getLocalTime(7) + " | " + Thread.currentThread().getName() + "] " + message;
    }
    
    public boolean logExists() {
        return log.exists();
    }
    
    public File getLog() {
        return log;
    }
}

