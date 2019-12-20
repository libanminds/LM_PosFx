package com.libanminds.repositories;

import com.libanminds.models.Role;
import com.libanminds.models.User;
import com.libanminds.utils.Authorization;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UsersRepository {

    public static boolean login(String username, String password) {
        //  TODO: check credentials.

        String query = "SELECT * FROM permissions WHERE role_id = " + 1;

        ArrayList<String> data = new ArrayList<>();
        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(rs.getString("permission"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        Authorization.Authorize(data);
        return true;
    }

    public static ObservableList<User> getUsers() {
        String query = "SELECT * FROM users JOIN role ON users.role_id = role.id ";

        return getUsersFromQuery(query);
    }

    public static ObservableList<User> getUsersLike(String value) {
        String query = "SELECT * FROM users JOIN role ON users.role_id = role.id WHERE" +
                " username like '%" + value + "%' or" +
                " first_name like '%" + value + "%' or" +
                " last_name like '%" + value + "%' or" +
                " email like '%" + value + "%' or" +
                " name like '%" + value + "%' or" +
                " phone like '%"+ value + "%'";

        return getUsersFromQuery(query);
    }

    public static boolean usernameExists(String username) {
        String query = "SELECT * FROM users where username = '" + username + "'";
        Statement statement = DBConnection.instance.getStatement();
        try {
            return statement.executeQuery(query).next();
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static boolean addUser(User user) {
        String query = "INSERT INTO users(username,first_name,last_name,password,email,phone,role_id,address) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhone());
            statement.setInt(7, user.getRoleID());
            statement.setString(8, user.getAddress());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUser(User user) {
        String query = "UPDATE users SET username = ?, first_name = ? , last_name = ?, password = ?  , email = ?, phone = ?, role_id = ? , address = ? where id = ?";

        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhone());
            statement.setInt(7, user.getRoleID());
            statement.setString(8, user.getAddress());
            statement.setInt(9, user.getID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUser(User user) {
        try {
            String query = "DELETE FROM users where id = " + user.getID();
            Statement statement  = DBConnection.instance.getStatement();
            statement.executeUpdate(query);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private static ObservableList<User> getUsersFromQuery(String query) {
        ObservableList<User> data = FXCollections.observableArrayList();
        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new User(
                        rs.getInt(1), // user ID
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        new Role(rs.getInt("role_id"), rs.getString("name")),
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }


}
