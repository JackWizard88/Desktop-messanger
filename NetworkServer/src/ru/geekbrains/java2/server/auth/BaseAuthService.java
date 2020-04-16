package ru.geekbrains.java2.server.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class BaseAuthService implements AuthService {

    private static Connection sqlconnection;
    private static final String USERDATA_DATABASE = "userDB.db" ;
    private static final Logger logger = LogManager.getLogger(BaseAuthService.class);

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
            logger.info(username + " changed name to " + newNickname);
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
    public void registerNewUser(String login, String password, String nickname) {

        try {
            String sql = "SELECT EXISTS(SELECT * FROM userData WHERE Login = ?)";
            PreparedStatement statement = sqlconnection.prepareStatement( sql );
            statement.setString( 1, login);

            ResultSet rs = statement.executeQuery();

            if (rs.getInt(1) != 0) {
                logger.error(String.format("error creating new user with %s, login is already in use", login));
                throw new RuntimeException("Такой пользователь уже существует!");
            } else {
                String sql2 = "INSERT INTO userData (Login, Password, Username) VALUES (?, ?, ?)";
                PreparedStatement statement2 = sqlconnection.prepareStatement(sql2);
                statement2.setString( 1, login);
                statement2.setString( 2, password);
                statement2.setString( 3, nickname);
                statement2.execute();
                logger.info(String.format("NEW USER CREATED LOGIN: %s, USERNAME: %s", login, nickname));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

            if (rs.getBoolean(5)) return null;
            else if (rs.getString(4).equals(password)) {
                username = rs.getString(2);
                String sql1 = "UPDATE userData SET Logged = true WHERE Login = ?";
                PreparedStatement statement1 = sqlconnection.prepareStatement(sql1);
                statement1.setString( 1, login);
                statement1.execute();
            }

        } catch (SQLException e) {
            logger.error("Invalid auth data. Auth refused");

        }
        return username;
    }

    private void createTableIfNotExist() {

        String sql = "CREATE TABLE IF NOT EXISTS userData (\n" +
                "    id       INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                     UNIQUE,\n" +
                "    Username STRING  NOT NULL,\n" +
                "    Login    STRING  NOT NULL ON CONFLICT FAIL,\n" +
                "    Password STRING  NOT NULL,\n" +
                "    Logged   BOOLEAN DEFAULT False\n" +
                ");";

        try {
            Statement stm = sqlconnection.createStatement();
            stm.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.error("Unable to create table in database");
        }
    }

    @Override
    public void start() {
        try {
            connectSQL();
            createTableIfNotExist();
            String sql2 = "UPDATE userData SET Logged = 0";
            Statement statement = sqlconnection.createStatement();
            statement.execute(sql2);
            logger.info("UserData successfully connected");
        } catch (SQLException e) {
            logger.error("Error while reading UserData database");
        }
        logger.info("Auth service started");
    }


    @Override
    public void stop() {

        try {
            sqlconnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Auth service stopped");
    }
}
