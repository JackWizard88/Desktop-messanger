package ru.geekbrains.java2.server.client;

import ru.geekbrains.java2.server.NetworkServer;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private final NetworkServer networkServer;
    private final Socket clientSocket;

    private DataInputStream in;
    private DataOutputStream out;

    private String nickname;

    public ClientHandler(NetworkServer networkServer, Socket socket) {
        this.networkServer = networkServer;
        this.clientSocket = socket;
    }

    public void run() {
        doHandle(clientSocket);
    }

    private void doHandle(Socket socket) {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    authentication();
                    readMessages();
                } catch (IOException e) {
                    System.out.println("Connection with " + nickname + " was closed!");
                } finally {
                    closeConnection();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            networkServer.unsubscribe(this);
            networkServer.sendMessage(nickname + " disconnected", this, "/all");
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return this.nickname;
    }

    private void readMessages() throws IOException {
        while (true) {
            String message = in.readUTF();
            if (message.startsWith("/w")) {
                String[] messageParts = message.split("\\s+", 3);
                String toNickname = messageParts[1];
                String messageText = messageParts[2];
                System.out.printf("private message from %s to %s: %s%n", nickname, toNickname, messageText);
                networkServer.sendMessage("private from " + nickname + ": " + messageText, this, toNickname);
            } else {
                System.out.printf("Всем от %s: %s%n", nickname, message);
                if ("/end".equals(message)) {
                    return;
                }
                networkServer.sendMessage(nickname + ": " + message, this, "/all");
            }
        }
    }

    private synchronized void authentication() throws IOException {
        while (true) {
            String message = in.readUTF();
            // "/auth login password"
            if (message.startsWith("/auth")) {
                String[] messageParts = message.split("\\s+", 3);
                String login = messageParts[1];
                String password = messageParts[2];
                String username = networkServer.getAuthService().getUsernameByLoginAndPassword(login, password);
                if (username == null) {
                    sendMessage("No such login&username data");
                } else {
                    nickname = username;
                    sendMessage("/auth " + nickname);
                    networkServer.sendMessage(nickname + " connected", this, "/all");
                    networkServer.subscribe(this);
                    break;
                }
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }
}
