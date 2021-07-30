package com.shopping.db;

import com.shopping.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {

    private static final Logger LOG = Logger.getLogger(UserDao.class.getName());

    public User getDetails(String username) {
        User user = new User();

        try {
            Connection conn = H2DatabaseConnection.getConnectionToDatabase();

            PreparedStatement ps = conn.prepareStatement("select * from user where username =?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setUsername(rs.getString("username"));
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to get details from user table for username :{}", username);
        }
        return user;
    }
}
