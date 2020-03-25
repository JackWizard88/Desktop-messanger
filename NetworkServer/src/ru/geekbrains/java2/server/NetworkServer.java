package ru.geekbrains.java2.server;

import ru.geekbrains.java2.server.auth.AuthService;
import ru.geekbrains.java2.server.auth.BaseAuthService;
import ru.geekbrains.java2.server.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkServer {

    private final int port;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final AuthService authService;

    public NetworkServer(int port) {
        this.port = port;
        this.authService = new BaseAuthService();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            authService.start();
            while (true) {
                System.out.println("Connection awaiting...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                createClientHandler(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        } finally {
            authService.stop();
        }
    }

    private void createClientHandler(Socket clientSocket) {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.run();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public synchronized void sendMessage(String message, ClientHandler owner, String acceptor) throws IOException {
        for (ClientHandler client : clients) {
            if (acceptor.equals("/all")) {
                if (client != owner) client.sendMessage(message);
            } else if (client != owner && client.getNickname().equals(acceptor)) {
                client.sendMessage(message);
            }
        }
    }

    public synchronized void subscribe(ClientHandler clientHandler) throws IOException {
        clients.add(clientHandler);
        for (ClientHandler client : clients) {
            client.sendMessage("/userList" + " clear all");
            for (ClientHandler client1 : clients) {
                client.sendMessage("/userList" + " " + "add" + " " + client1.getNickname());
            }
        }
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) throws IOException {
        clients.remove(clientHandler);
        if (!clients.isEmpty()) {
            for (ClientHandler client : clients) {
                client.sendMessage("/userList" + " remove " + clientHandler.getNickname());
            }
        }
    }

}
