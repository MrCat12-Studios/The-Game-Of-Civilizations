package com.mrcat.civilizations;

import java.util.Scanner;
import java.net.UnknownHostException;

import com.mrcat.civilizations.debug.Logging;

public class Main {
    
    Logging logging = Logging.getInstance();
    
    public static void main(String[] args) {
        Main main = new Main();
        ClientSocket clientSocket = new ClientSocket();
        Scanner scanner = new Scanner(System.in);
        String address;
        int port;
        while (true) try {
            System.out.println("Enter IP address:\n");
            address = scanner.nextLine();
            System.out.println("Enter port:\n");
            port = Integer.parseInt(scanner.nextLine());
            clientSocket.startClient(address, port);
            break;
        }
        catch (UnknownHostException ex) {
            main.logging.addLog(ex.getMessage(), main.logging.logExists());
        }
    }
    
}