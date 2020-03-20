package ru.geekbrains.java2.client.model;

import ru.geekbrains.java2.client.controller.AuthEvent;
import ru.geekbrains.java2.client.controller.fxview.FxChatWindow;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NetworkService {

    private final String host;
    private final int port;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private Consumer<String> messageHandler;
    private FxChatWindow curentWindow;
    private AuthEvent successfulAuthEvent;
    private String nickname;
    private List<String> userlist = new ArrayList<>();

    public NetworkService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        runReadThread();
    }

    private void runReadThread() {
        new Thread(() -> {
            while (true) {
                try {
                    String message = in.readUTF();
                    if (message.startsWith("/auth")) {
                        String[] messageParts = message.split("\\s+", 2);
                        nickname = messageParts[1];
                        successfulAuthEvent.authIsSuccessful(nickname);
                    }
                    else if (message.startsWith("/userList")) {
                        String[] messageParts = message.split("\\s+", 3);
                        String event = messageParts[1];
                        String data = messageParts[2];
                        if (event.equals("add")) {
                            userlist.add(data);
                            curentWindow.updateUserListField(userlist);
                        } else if (event.equals("remove")) {
                            userlist.remove(data);
                            curentWindow.updateUserListField(userlist);
                        }
                    }
                    else if (messageHandler != null) {

                        //messageHandler.accept(message);
                    }
                    else curentWindow.appendMessage(message);
                } catch (IOException e) {
                    System.out.println("ReadThread was interrupted");
                    return;
                }
            }
        }).start();
    }

    public void sendAuthMessage(String login, String password) throws IOException {
        out.writeUTF(String.format("/auth %s %s", login, password));
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

    public void setMessageHandler(Consumer<String> messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setCurentWindow(FxChatWindow curentWindow) {
        this.curentWindow = curentWindow;
    }

    public void setSuccessfulAuthEvent(AuthEvent successfulAuthEvent) {
        this.successfulAuthEvent = successfulAuthEvent;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
