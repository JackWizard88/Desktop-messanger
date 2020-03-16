package Lesson6.Server;


import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static final String END_COMMAND = "/end";
    private Socket clientSocket = null;
    private ServerSocket serverSocket = null;
    private DataInputStream in;
    private DataOutputStream out;
    private HashMap<User, Socket> userlist;

    public Server() {
        userlist = new HashMap<>();
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
            new Server();
    }

    public void startServer() throws IOException {

        serverSocket = new ServerSocket(8188);

        Thread connectionListener = new Thread(() -> {
            try {
                conListener();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        connectionListener.start();

    }

    public void conListener() throws IOException {

        System.out.println("Сервер запущен");
        while (true) {
            clientSocket = serverSocket.accept();
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            String name = in.readUTF();
            User user = new User(name, in, out, clientSocket);
            userlist.put(user, clientSocket);
            System.out.println(name + " подключился");
            new Thread(() -> messageReader(user)).start();
            new Thread(() -> messageSender()).start();

        }
    }

    public void messageReader(User user) {

        while (true) {
            String message = null;
            try {
                message = user.getIn().readUTF();
                if (message.equals(END_COMMAND)) {
                    break;
                }
            } catch (SocketException e) {
                try {
                    System.out.println(user.getName() + " отключился");
                    user.getSocket().close();
                    userlist.remove(user);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(user.getName() + ": " + message);
            for (Map.Entry<User, Socket> entry : userlist.entrySet()) {
                if(!user.equals(entry.getKey())) {
                    try {
                        entry.getKey().getOut().writeUTF(entry.getKey().getName() + ": " + message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    public void messageSender() {

            while (true) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String msg = null;
                try {
                    msg = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!msg.trim().isEmpty()) {
                    try {
                        for (Map.Entry<User, Socket> entry : userlist.entrySet()) {
                            entry.getKey().getOut().writeUTF("Server: " + msg);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Ошибка отправки сообщения");
                    }
                }
            }


    }

}
