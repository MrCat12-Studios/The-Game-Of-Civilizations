package com.mrcat.civilizations;

import java.util.Map;
import java.util.HashMap;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import com.mrcat.civilizations.debug.Logging;
import com.mrcat.civilizations.entity.Entity;
import com.mrcat.civilizations.main.Player;

public class GameServer {

    Logging logging = Logging.getInstance();
    Thread serverThread;
    BufferedReader in;
    PrintWriter out;
    Map<String, Player> players = new HashMap<>();

    public void startServer() {
        serverThread = new Thread(() -> {
            try (ServerSocket server = new ServerSocket(0)) {
                logging.addLog("Server running on port " + server.getLocalPort(), logging.logExists());
                while (true) {
                    Socket client = server.accept();
                    logging.addLog("Client " + client.getInetAddress().toString() + " connected", logging.logExists());
                    Thread clientThread = new Thread(() -> handleClient(client));
                    clientThread.start();
                }
            }
            catch (IOException ex) {
                logging.addLog(ex.getMessage(), logging.logExists());
            }
        });
        serverThread.start();
    }

    private void handleClient(Socket client) {
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8);
            out.println("username");
            String name = in.readLine();
            out.println("password");
            String password = in.readLine();
            players.put(client.getInetAddress().toString(), new Player(name, password, Thread.currentThread()));
            out.println("start");
            String content;
            while (!client.isClosed()) {
                while ((content = in.readLine()) != null) translateInput(content);
            }
            logging.addLog("Client " + client.getInetAddress().toString() + " disconnected", logging.logExists());
            for (Player i : players.values()) if (i.thread == Thread.currentThread()) {
                players.remove(client.getInetAddress().toString());
            }
        }
        catch (Exception ex) {
            for (Player i : players.values()) if (i.thread == Thread.currentThread()) {
                players.remove(client.getInetAddress().toString());
            }
            logging.addLog(ex.getMessage(), logging.logExists());
        }
    }

    private void translateInput(String input) {
        String comm = input.substring(0, 4);
        String target = input.substring(5, 11);
        String arg = input.substring(12);
        switch (comm) { // TODO
            case "move":
                try {
                    if (Entity.entities.containsKey(target)) Entity.entities.get(target).moveTo(Integer.parseInt(arg.substring(0, 2)), Integer.parseInt(arg.substring(3, 5)));
                    else break;
                }
                catch (Exception ex) { // TODO
                    
                }
                out.println(translateOutput(comm, target, arg));
                break;
            
        }
    }

    private String translateOutput(String comm, String target, String arg) {
        return comm + ":" + target + ":" + arg;
    }

    public void getClient() {
        
    }
}