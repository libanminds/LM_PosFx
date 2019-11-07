package com.libanminds.repositories;

import com.libanminds.models.Supplier;
import com.libanminds.models.User;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private static ObservableList<User> getUsersFromQuery(String query) {
        ObservableList<User> data = FXCollections.observableArrayList();
        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new User(
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
