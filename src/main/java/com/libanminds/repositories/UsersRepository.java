package com.libanminds.repositories;

import com.libanminds.models.Expense;
import com.libanminds.models.Income;
import com.libanminds.models.User;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsersRepository {

    public static ObservableList<User> getUsers() {
        String query = "SELECT * FROM users";

        return getUsersFromQuery(query);
    }

    public static ObservableList<User> getUsersLike(String value) {
        String query = "SELECT * FROM users where" +
                " username like '%" + value + "%' or" +
                " first_name like '%" + value + "%' or" +
                " last_name like '%" + value + "%' or" +
                " email like '%" + value + "%' or" +
                " role like '%" + value + "%' or" +
                " phone like '%"+ value + "%'";

        return getUsersFromQuery(query);
    }

    public static boolean addUser(User user) {
        String query = "INSERT INTO users(username,first_name,last_name,password,email,phone,role,address) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhone());
            statement.setString(7, user.getRole());
            statement.setString(8, user.getAddress());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUser(User user) {
        String query = "UPDATE users SET username = ?, first_name = ? , last_name = ?, password = ?  , email = ?, phone = ?, role = ? , address = ? where id = ?";

        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhone());
            statement.setString(7, user.getRole());
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
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("role"),
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
