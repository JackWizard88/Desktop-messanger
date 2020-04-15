package ru.geekbrains.java2.server.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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


public class ClientHandler {

    private final NetworkServer networkServer;
    private final Socket clientSocket;
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String nickname;
    private String login;

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

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
                    logger.info("Connection with " + nickname + " was closed!");
//                    System.out.println("Connection with " + nickname + " was closed!");
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
                    String pr = "";
                    if (!receiver.equals("all")) pr = "(private)";
                    String message = String.format("%s %s %s: %s", getTime(), pr, getNickname(), commandData.getMessage());
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
                    logger.error("Unknown type of command : " + command.getType());
//                    System.err.println("Unknown type of command : " + command.getType());
            }
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type of object from client!";
            logger.error(errorMessage);
//            System.err.println(errorMessage);
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

    private synchronized void authentication() throws IOException {

        Thread authKiller = new Thread(() -> {
            try {
                Thread.sleep(120000);
                logger.info("Client disconnected by timeout");
//                System.out.println("Client disconnected by timeout");
                closeConnection();
            } catch (InterruptedException e) {
                logger.info("Auth successful");
//                System.out.println("Auth successful");
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
                logger.error("Unknown type of command for auth process: " + command.getType());
//                System.err.println("Unknown type of command for auth process: " + command.getType());
            }
        }
    }

    private boolean processAuthCommand(Command command) throws IOException {
        AuthCommand commandData = (AuthCommand) command.getData();
        login = commandData.getLogin();
        String password = commandData.getPassword();
        String username = networkServer.getAuthService().getUsernameByLoginAndPassword(login, password);
        logger.info("AuthData via Login: {}, Password: {}", login, password);
        if (username == null) {
            logger.error("Invalid auth data or user is allready online");
            Command errorCommand = Command.errorCommand("Отсутствует учетная запись или пользователь уже в сети");
            sendMessage(errorCommand);
            return false;
        }
        else {
            nickname = username;
            String message = String.format("%s %s зашел в чат!",getTime(), nickname);
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
        return "[" + formatForDateNow.format(dateNow) + "]";
    }
}
