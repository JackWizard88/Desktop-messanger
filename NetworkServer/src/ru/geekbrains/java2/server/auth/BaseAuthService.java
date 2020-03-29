package ru.geekbrains.java2.server.auth;

import java.sql.*;

public class BaseAuthService implements AuthService {

    private static Connection sqlconnection;

    public static void connectSQL() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            sqlconnection = DriverManager.getConnection("jdbc:sqlite:userDB.db");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void changeNickName(String username, String newNickname) {
        try {
            connectSQL();
            String sql = "UPDATE userData SET Username = ? WHERE Login = ?";
            PreparedStatement statement = sqlconnection.prepareStatement( sql );
            System.out.println(username + " " + newNickname);
            statement.setString( 1, newNickname);
            statement.setString( 2, username);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                sqlconnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized String getUsernameByLoginAndPassword(String login, String password) {

        String username = null;

        try {
            connectSQL();
            String sql = "SELECT * FROM userData WHERE Login = ?";
            PreparedStatement statement = sqlconnection.prepareStatement( sql );
            statement.setString( 1, login);

            ResultSet rs = statement.executeQuery();

            if (rs.getString(4).equals(password)) {
                username = rs.getString(2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                sqlconnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return username;
    }

    @Override
    public void start() {
        System.out.println("Auth service started");
    }

    @Override
    public void stop() {
        System.out.println("Auth service stopped");
    }
}
