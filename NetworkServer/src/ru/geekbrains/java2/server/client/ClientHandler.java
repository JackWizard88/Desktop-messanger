package ru.geekbrains.java2.server.client;

import ru.geekbrains.java2.server.NetworkServer;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if (nickname != null) {
                networkServer.unsubscribe(this);
                networkServer.sendMessage(nickname + " disconnected", this, "/all");
            }
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
                //System.out.printf("private message from %s to %s: %s%n", nickname, toNickname, messageText);
                networkServer.sendMessage("private from " + nickname + ": " + messageText, this, toNickname);
            } else if (message.startsWith("/newNick")) {
                String[] messageParts = message.split("\\s+", 3);
                String login = messageParts[1];
                String newNickname = messageParts[2];
                networkServer.getAuthService().changeNickName(login, newNickname);
                networkServer.sendMessage(getTime() + nickname + " changed name to " + newNickname, null, "/all");
                networkServer.changeNickname(nickname, newNickname);
                nickname = newNickname;
            } else {
                message = censorMessage(message);
                networkServer.sendMessage(getTime() + nickname + ": " + message, this, "/all");
            }
        }
    }

    private String censorMessage(String message) {
        final String[] forbidden = { "блядь",
                "бля",
                "выеб",
                "гомо",
                "долбо",
                "ебло",
                "ебли",
                "ебать",
                "ебич",
                "ебуч",
                "ебун",
                "ебла",
                "ебну",
                "ебол",
                "ебош",
                "ебал",
                "ебат",
                "ебуч",
                "заёб",
                "залуп",
                "залупо",
                "ебин",
                "манда",
                "мандо",
                "ъеби",
                "хуе",
                "пизда",
                "пидар",
                "пидор",
                "залуп",
                "пизд",
                "сука",
                "сучка",
                "трах",
                "уебок",
                "уебать",
                "гондо",
                "гандо",
                "уебан",
                "хуй",
                "хуи",
                "членосос",
                "член",
                "шлюх"};

        for(int i = 0; i < forbidden.length; i++) {
            Pattern pattern = Pattern.compile("(\\w*)" + forbidden[i] + "(\\w*)");
            Matcher matcher = pattern.matcher(message);
            message = matcher.replaceAll("***");
        }
        return message;


    }

    private synchronized void authentication() throws IOException {

        Thread authKiller = new Thread(() -> {
            try {
                Thread.sleep(120000);
                System.out.println("Client disconnected by timeout");
                closeConnection();
            } catch (InterruptedException e) {
                System.out.println("Auth successful");
            }
        });

        authKiller.start();

        while (true) {
            String message = in.readUTF();
            // "/auth login password"
            if (message.startsWith("/auth")) {
                String[] messageParts = message.split("\\s+", 3);
                String login = messageParts[1];
                String password = messageParts[2];
                String username = networkServer.getAuthService().getUsernameByLoginAndPassword(login, password);
                if (username == null) {
                    sendMessage("/err " + "incorrect account data");
                } else {
                    authKiller.interrupt();
                    nickname = username;
                    sendMessage("/auth " + nickname);
                    networkServer.sendMessage(getTime() + nickname + " connected", this, "/all");
                    networkServer.subscribe(this);
                    break;
                }
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

    private String getTime() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss");
        return "[" + formatForDateNow.format(dateNow) + "] ";
    }
}
