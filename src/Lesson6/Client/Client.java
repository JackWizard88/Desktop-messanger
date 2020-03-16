package Lesson6.Client;

import Lesson6.Server.User;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8188;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Client() {
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        messageSender();

    }

    public static void main(String[] args) {

        new Client();

    }

    public void openConnection() throws IOException {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        String name = JOptionPane.showInputDialog("Введите имя пользователя");
        out.writeUTF(name);
        new Thread(() -> {
            try {
                while (true) {
                    String strFromServer = in.readUTF();
                    if (strFromServer.equalsIgnoreCase("/end")) {
                        closeConnection();
                        break;
                    }
                    System.out.println(strFromServer);
                }
            } catch (Exception e) {
                System.out.println("Connection has been closed!");
            }
        }).start();
    }

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void messageSender() {

        new Thread(() -> {
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
                        out.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Ошибка отправки сообщения");
                    }
                }
            }
        }).start();
    }

}
