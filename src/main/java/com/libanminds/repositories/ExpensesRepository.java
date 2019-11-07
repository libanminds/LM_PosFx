package com.libanminds.repositories;

import com.libanminds.models.Expense;
import com.libanminds.models.User;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExpensesRepository {

    public static ObservableList<Expense> getExpenses() {
        String query = "SELECT * FROM expenses";

        return getExpensesFromQuery(query);
    }

    public static ObservableList<Expense> getExpensesLike(String value) {
        String query = "SELECT * FROM expenses where" +
                " description like '%" + value + "%' or" +
                " recipient like '%" + value + "%' or" +
                " notes like '%"+ value + "%'";

        return getExpensesFromQuery(query);
    }

    private static ObservableList<Expense> getExpensesFromQuery(String query) {
        ObservableList<Expense> data = FXCollections.observableArrayList();
        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);

            while (rs.next()) {
                data.add(new Expense(
                        "Type goes here",
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getString("currency"),
                        rs.getString("payment_type"),
                        rs.getString("recipient"),
                        rs.getString("notes")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
