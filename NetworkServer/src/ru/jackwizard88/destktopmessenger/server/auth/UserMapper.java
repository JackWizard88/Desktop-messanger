package ru.jackwizard88.destktopmessenger.server.auth;

import ru.jackwizard88.destktopmessenger.server.Entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    public static User map(ResultSet rs) {

        User user = new User();

        while(true) {
            try {
                if (!rs.next()) break;
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setLogin(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setLoggedIn(rs.getBoolean(5));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return user;
    }

}
