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
import com.mrcat.civilizations.entity.*;
import com.mrcat.civilizations.Player;
import com.mrcat.civilizations.IO.*;
import com.mrcat.civilizations.resources.*;
import com.mrcat.civilizations.map.*;

public class GameServer {

    Logging logging = Logging.getInstance();
    ResourceHandler rh = new ResourceHandler();
    JsonHandler jh = new JsonHandler();
    Thread serverThread;
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
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8);
            String name = "";
            String password = "";
            Player player;
            out.println("auth");
            String auth = in.readLine();
            out.println("username");
            name = in.readLine();
            out.println("password");
            password = in.readLine();
            if ("register".equals(auth)) {
                player = new Player(name, password, Thread.currentThread(), client);
                player.register();
            }
            else { // case login
                player = new Player(name, password, Thread.currentThread(), client);
                player.login();
            }
            players.put(client.getInetAddress().toString(), player);
            out.println("start");
            String content;
            if ((content = in.readLine()) != null && "world".equals(content)) out.println(rh.read("worlds/" + World.name + "/world.json"));
            Thread entityLifecycleThread = new Thread(() -> {
                while (true) {
                    if (Thread.interrupted()) break;
                    for (Entity e : Entity.entities.values()) if (e.isUpdated) send("get", e.id, jh.buildJson(e.json), out);
                }
            });
            entityLifecycleThread.start();
            while ((content = in.readLine()) != null) translateInput(content, out);
            // EOF
            logging.addLog("Client " + client.getInetAddress().toString() + " disconnected", logging.logExists());
            for (Player i : players.values()) if (i.thread.equals(Thread.currentThread())) {
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

    private void translateInput(String input, PrintWriter out) {
        String comm = input.substring(0, 3);
        String target = input.substring(4, 10);
        String arg = input.substring(11);
        boolean containsTarget = Entity.entities.containsKey(target);
        Entity entity = Entity.DEFAULT;
        if (containsTarget) entity = Entity.entities.get(target);
        switch (comm) {
            case "mov":
                if (containsTarget && entity instanceof Moveable m) {
                    m.moveTo(Integer.parseInt(arg.substring(0, 2)), Integer.parseInt(arg.substring(3, 5)));
                }
                break;
            case "atk":
                if (containsTarget && entity instanceof Attackable a) {
                    a.attack(Entity.entities.get(arg));
                    send(comm, target, arg, out);
                }
                break;
            case "giv":
                send("get", target, jh.buildJson(entity.json), out);
                break;
            case "use":
                if (containsTarget && entity instanceof Gearable g) {
                    g.use();
                }
                break;
            case "add":
                if (containsTarget && entity instanceof Inventoryable i) {
                    if (entity instanceof Gearable g) g.addItem(jh.generateResource(jh.parseJson("items/" + arg.substring(2) + ".json")), g.SLOTS[Integer.parseInt(arg.substring(0, 1))]);
                    else i.addItem(jh.generateResource(jh.parseJson("items/" + arg + ".json")));
                }
                break;
            case "rem":
                if (containsTarget && entity instanceof Inventoryable i) i.removeItem(Integer.parseInt(arg));
                break;
            case "slp":
                if (containsTarget && entity instanceof Sleepable s) s.sleep();
                break;
            case "pic":
                if (containsTarget && entity instanceof Pickable p && Entity.entities.get(arg) instanceof Collectable c) p.pick(c);
                break;
            case "min":
                Item item = jh.generateResource(jh.parseJson("items/" + arg + ".json", true));
                if (containsTarget && entity instanceof Human h) h.mine(item);
            case "cra":
                Recipe recipe = jh.generateRecipe(jh.parseJson("recipies/" + arg.substring(6)));
                Building building = (Building) jh.generateEntity(jh.parseJson("entities/" + arg.substring(0, 6)));
                if (containsTarget && entity instanceof Craftable c) c.craft(recipe, building);
        }
    }

    public String translateOutput(String comm, String target, String arg) {
        return comm + ":" + target + ":" + arg;
    }

    public void send(String comm, String target, String arg, PrintWriter out) {
        out.println(translateOutput(comm, target, arg));
    }

    public void getClient() {
        
    }
}