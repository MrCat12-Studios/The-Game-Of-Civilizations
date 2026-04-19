package com.mrcat.civilizations;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import com.mrcat.civilizations.debug.Logging;
import com.mrcat.civilizations.entity.Entity;
import com.mrcat.civilizations.map.*;
import com.mrcat.civilizations.IO.*;
import com.google.gson.JsonArray;

public class ClientSocket {
    
    Logging logging = Logging.getInstance();
    JsonHandler jh = new JsonHandler();

    BufferedReader in;
    PrintWriter out;

    public void startClient(String address, int port) throws UnknownHostException {
        try {
            Socket socket = new Socket(address, port);
            Thread thread = new Thread(() -> {
                handleConnection(socket);
                logging.addLog("Disconnected", logging.logExists());
                return;
            });
            thread.start();
        }
        catch (UnknownHostException ex) {
            throw ex;
        }
        catch (IOException ex) {
            logging.addLog(ex.getMessage(), logging.logExists());
        }
    }

    public void handleConnection(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            Scanner scanner = new Scanner(System.in);
            if ("username".equals(in.readLine())) {
                System.out.println("Enter username: ");
                out.println(scanner.nextLine());
            }
            if ("password".equals(in.readLine())) {
                System.out.println("Enter password: ");
                out.println(scanner.nextLine());
            }
            if (!"start".equals(in.readLine())) return;
            out.println("world");
            Json worldJson = jh.parseJson(in.readLine());
            JsonArray rows = worldJson.attributes.get("map").getAsJsonArray();
            Chunk[][] chunks = new Chunk[180][240];
            for (int y = 0; y < 180; y++) {
                JsonArray row = rows.get(y).getAsJsonArray();
                for (int x = 0; x < 240; x++) {
                    chunks[y][x] = new Chunk(row.get(x).getAsString());
                }
            }
            new World(worldJson.name, chunks);
            String content;
            while ((content = in.readLine()) != null) translateInput();
            // EOF
            System.out.println("Disconnected");
            return;
        }
        catch (IOException ex) {
            logging.addLog(ex.getMessage(), logging.logExists());
        }
    }

    public translateInput(String input) {
        String comm = input.substring(0, 3);
        String target = input.substring(4, 10);
        String arg = input.substring(11);
        boolean containsTarget = Entity.entities.containsKey(target);
        Entity entity = Entity.DEFAULT;
        if (containsTarget) entity = Entity.entities.get(target);
        if ("get".equals(comm)) {
            if (!containsTarget) jh.generateEntity(jh.parseJson(arg));
            else Entity.entities.replace(target, jh.generateEntity(jh.parseJson(arg)));
        }
    }
}