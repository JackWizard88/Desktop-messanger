package ru.geekbrains.java2.server.client;

import ru.geekbrains.java2.commands.CTypeEnum;
import ru.geekbrains.java2.commands.Command;
import ru.geekbrains.java2.commands.command.AuthCommand;
import ru.geekbrains.java2.commands.command.ChangeNicknameCommand;
import ru.geekbrains.java2.commands.command.MessageCommand;
import ru.geekbrains.java2.server.NetworkServer;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientHandler {

    private final NetworkServer networkServer;
    private final Socket clientSocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String nickname;
    private String login;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public ClientHandler(NetworkServer networkServer, Socket socket) {
        this.networkServer = networkServer;
        this.clientSocket = socket;
    }

    public void run() {
        doHandle(clientSocket);
    }

    private void doHandle(Socket socket) {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            executorService.execute(() -> {
                try {
                    authentication();
                    readData();
                } catch (IOException e) {
                    System.out.println("Connection with " + nickname + " was closed!");
                } finally {
                    closeConnection();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case MESSAGE: {
                    MessageCommand commandData = (MessageCommand) command.getData();
                    String receiver = commandData.getReceiver();
                    String message = String.format("%s %s: %s", getTime(), getNickname(), commandData.getMessage());
                    networkServer.sendMessage(this, receiver, Command.messageCommand(nickname, message, receiver));
                    break;
                }
                case CHANGE_NICKNAME: {
                    ChangeNicknameCommand commandData = (ChangeNicknameCommand) command.getData();
                    String login = commandData.getLogin();
                    nickname = commandData.getUsername();
                    networkServer.getAuthService().changeNickName(login, nickname);
                    networkServer.updateUserList();
                    break;
                }
                default:
                    System.err.println("Unknown type of command : " + command.getType());
            }
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type of object from client!";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Command.errorCommand(errorMessage));
            return null;
        }
    }

    private void closeConnection() {
        try {
            if (nickname != null) {
                networkServer.unsubscribe(this);
                networkServer.getAuthService().logOut(login);
                String message = getTime() + nickname + " disconnected";
                String receiver = "all";
                networkServer.sendMessage( this, receiver, Command.messageCommand(nickname, message, receiver));
                executorService.shutdown();
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return this.nickname;
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
                "муда",
                "мудо",
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
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CTypeEnum.AUTH) {
                boolean successfulAuth = processAuthCommand(command);
                if (successfulAuth){
                    authKiller.interrupt();
                    return;
                }
            } else {
                System.err.println("Unknown type of command for auth process: " + command.getType());
            }
        }
    }

    private boolean processAuthCommand(Command command) throws IOException {
        AuthCommand commandData = (AuthCommand) command.getData();
        login = commandData.getLogin();
        String password = commandData.getPassword();
        String username = networkServer.getAuthService().getUsernameByLoginAndPassword(login, password);
        if (username == null) {
            Command errorCommand = Command.errorCommand("Отсутствует учетная запись или пользователь уже в сети");
            sendMessage(errorCommand);
            return false;
        }
        else {
            nickname = username;
            String message = getTime() + nickname + " зашел в чат!";
            String receiver = "all";
            networkServer.sendMessage(this, receiver, Command.messageCommand(null, message, receiver));
            commandData.setUsername(nickname);
            sendMessage(command);
            networkServer.subscribe(this);
            return true;
        }
    }

    public void sendMessage(Command command) throws IOException {
        out.writeObject(command);
    }

    private String getTime() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss");
        return "[" + formatForDateNow.format(dateNow) + "] ";
    }
}
