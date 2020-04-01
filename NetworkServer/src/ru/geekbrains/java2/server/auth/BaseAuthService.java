package ru.geekbrains.java2.server.auth;

import java.sql.*;

public class BaseAuthService implements AuthService {

    private static Connection sqlconnection;
    private static final String USERDATA_DATABASE = "userDB.db" ;

    public static void connectSQL() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            sqlconnection = DriverManager.getConnection("jdbc:sqlite:" + USERDATA_DATABASE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void changeNickName(String username, String newNickname) {
        try {
            String sql = "UPDATE userData SET Username = ? WHERE Login = ?";
            PreparedStatement statement = sqlconnection.prepareStatement( sql );
            System.out.println(username + " changed name to " + newNickname);
            statement.setString( 1, newNickname);
            statement.setString( 2, username);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized String getUsernameByLoginAndPassword(String login, String password) {

        String username = null;

        try {
            String sql = "SELECT * FROM userData WHERE Login = ?";
            PreparedStatement statement = sqlconnection.prepareStatement( sql );
            statement.setString( 1, login);

            ResultSet rs = statement.executeQuery();

            if (rs.getString(4).equals(password)) {
                username = rs.getString(2);
            }

        } catch (SQLException e) {
            System.err.println("Invalid auth data. Auth refused");
        }
        return username;
    }

    @Override
    public void start() {
        try {
            connectSQL();
            System.out.println("UserData successfully connected");
        } catch (SQLException e) {
            System.out.println("Error while reading UserData database");
        }
        System.out.println("Auth service started");
    }

    @Override
    public void stop() {
        try {
            sqlconnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Auth service stopped");
    }
}
