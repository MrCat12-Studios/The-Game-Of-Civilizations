package com.mrcat.civilizations.main;

import java.util.Map;
import java.util.HashMap;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.net.Socket;
import com.google.gson.JsonPrimitive;
import com.mrcat.civilizations.IO.*;
import com.mrcat.civilizations.map.World;

public class Player {

    private String name;
    private String password;
    private String hashed;
    public Thread thread;
    public Socket socket;
    ResourceHandler rh = new ResourceHandler();
    JsonHandler jh = new JsonHandler();

    public Player(String name, String password, Thread thread, Socket socket) {
        this.name = name;
        this.password = password;
        this.thread = thread;
        this.socket = socket;
    }

    public void register() {
        this.hashed = encrypt(password);
        Json json = jh.parseJson("worlds/" + World.name, false);
        json.attributes.put(name, new JsonPrimitive(this.hashed));
        jh.writeJson("worlds/" + World.name, json);
    }

    public void login() {
        
    }

    public String encrypt(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashed) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        }
        catch (Exception ex) {
            logging.addLog(ex.getMessage, logging.logExists());
            socket.close();
        }
    }

    public String getPassword() {
        return password;
    }

    public String getHashed() {
        return hashed;
    }

    public String getName() {
        return name;
    }
}