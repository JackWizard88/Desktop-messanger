package Lesson6.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class User {


    private String name;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;


    public User(String name, DataInputStream in, DataOutputStream out, Socket socket) {

        this.socket = socket;
        this.name = name;
        this.in = in;
        this.out = out;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public Socket getSocket() {
        return socket;
    }
}
