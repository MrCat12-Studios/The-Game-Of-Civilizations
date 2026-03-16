package com.mrcat.civilizations.IO;

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

public class ClientSocket {
    
    Logging logging = Logging.getInstance();

    public void startClient(String address, int port) throws UnknownHostException {
        try {
            Socket socket = new Socket(address, port);
            Thread thread = new Thread(() -> {
                handleConnection(socket);
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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            Scanner scanner = new Scanner(System.in);
            if ("username".equals(in.readLine())) {
                System.out.println("Enter username: ");
                out.println(scanner.nextLine());
            }
            if ("password".equals(in.readLine())) {
                System.out.println("Enter password: ");
                out.println(scanner.nextLine());
            }
            while (!"start".equals(in.readLine())) continue;
        }
        catch (IOException ex) {
            logging.addLog(ex.getMessage(), logging.logExists());
        }
    }
}