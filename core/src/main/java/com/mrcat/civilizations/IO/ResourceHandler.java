package com.mrcat.civilizations.IO;

import java.io.File;
import java.io.Reader;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.BufferedInputStream;
import com.mrcat.civilizations.debug.Logging;
import java.nio.charset.StandardCharsets;
import java.io.Closeable;

public class ResourceHandler {
    
    Logging logging = Logging.getInstance();
        
    public File newFile(String path) {
        File file = new File(path);
        try {
            if (!file.exists()) file.createNewFile();
            return file;
        }
        catch (IOException ex) {
            logging.addLog("Could not create file | " + ex.getMessage(), logging.logExists());
            return null;
        }
    }
    
    public void write(String path, String message, Boolean append) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, append))) {
            bw.write(message);
        }
        catch (IOException ex) {
            logging.addLog("Could not write to the selected file | " + ex.getMessage(), false);
        }
    }

    public String read(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String content = "";
            while ((content = br.readLine()) != null) content += br.readLine() + "\n";
        }
        catch (IOException ex) {
            logging.addLog("Could not read from the selected file | " + ex.getMessage(), false);
        }
        return null;
    }
    
    public Reader getReader(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        return br;
    }

    public InputStream getInputStream(String path) throws IOException {
        BufferedInputStream bs = new BufferedInputStream(new FileInputStream(path));
        return bs;
    }

    public byte[] read(String path, boolean fromJar) {
        if (fromJar) {
            try (BufferedInputStream bs = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(path))) {
                return bs.readAllBytes();
            }
            catch (IOException ex) {
                logging.addLog("Could not read from the selected file | " + ex.getMessage(), false);
            }
        }
        else {
            try (BufferedInputStream bs = new BufferedInputStream(new FileInputStream(path))) {
                
            } 
            catch (IOException ex) {}
        }
        return null;
    }
    
    public String convertBytes(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
    
    public void closeSafe(Closeable closeable) {
        try {
            closeable.close();
        }
        catch (IOException ex) {
            // ...
        }
    }
}