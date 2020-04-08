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
    public void logOut(String login) {
        try {
            String sql2 = "UPDATE userData SET Logged = false WHERE Login = ?";
            PreparedStatement statement2 = sqlconnection.prepareStatement(sql2);
            statement2.setString( 1, login);
            statement2.execute();
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

            if (rs.getString(4).equals(password) && !rs.getBoolean(5)) {
                username = rs.getString(2);
                String sql1 = "UPDATE userData SET Logged = true WHERE Login = ?";
                PreparedStatement statement1 = sqlconnection.prepareStatement(sql1);
                statement1.setString( 1, login);
                statement1.execute();

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
