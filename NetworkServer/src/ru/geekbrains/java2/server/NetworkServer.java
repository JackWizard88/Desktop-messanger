package ru.geekbrains.java2.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.java2.commands.Command;
import ru.geekbrains.java2.server.auth.AuthService;
import ru.geekbrains.java2.server.auth.BaseAuthService;
import ru.geekbrains.java2.server.client.ClientHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NetworkServer {

    private final int port;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final AuthService authService;
    private static final Logger logger = LogManager.getLogger(NetworkServer.class);
    private ServerSocket serverSocket;

    public NetworkServer(int port) {
        this.port = port;
        this.authService = new BaseAuthService();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Server STARTED on port " + port);
            authService.start();
            while (true) {
                logger.info("Connection awaiting...");
                Socket clientSocket = serverSocket.accept();
                logger.info("Client connected");
                createClientHandler(clientSocket);
            }
        } catch (SocketException se) {
            logger.info("Server Socket closed");
        } catch (IOException e) {
            logger.error("Error");
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

    public synchronized void sendMessage(ClientHandler owner, String receiver, Command message) throws IOException {

        for (ClientHandler client : clients) {
            if (receiver.equals("all")) {
                if (client != owner) client.sendMessage(message);
            } else if (client.getNickname().equals(receiver)) {
                client.sendMessage(message);
            }
        }
    }

    public synchronized void subscribe(ClientHandler clientHandler) throws IOException {
        clients.add(clientHandler);
        updateUserList();
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) throws IOException {
        clients.remove(clientHandler);
        updateUserList();
    }

    private List<String> getAllUsernames() {
        List<String> usernames = new LinkedList<>();
        for (ClientHandler clientHandler : clients) {
            usernames.add(clientHandler.getNickname());
        }
        return usernames;
    }

    public synchronized void updateUserList() throws IOException {
        List<String> users = getAllUsernames();
        sendMessage(null, "all", Command.updateUsersListCommand(users));
    }

    public int stop() {

        for (ClientHandler client : clients) {
            try {
                client.sendMessage(Command.errorCommand("Server shut down!"));
                client.getClientSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.info("Server STOPPED");
        return 1;
    }

}
