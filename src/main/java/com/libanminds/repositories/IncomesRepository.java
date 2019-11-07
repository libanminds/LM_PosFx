package com.libanminds.repositories;

import com.libanminds.models.Expense;
import com.libanminds.models.Income;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IncomesRepository {

    public static ObservableList<Income> getIncomes() {
        String query = "SELECT * FROM incomes";

        return getIncomesFromQuery(query);
    }

    public static ObservableList<Income> getIncomesLike(String value) {
        String query = "SELECT * FROM incomes where" +
                " description like '%" + value + "%' or" +
                " taken_from like '%" + value + "%' or" +
                " notes like '%"+ value + "%'";

        return getIncomesFromQuery(query);
    }

    private static ObservableList<Income> getIncomesFromQuery(String query) {
        ObservableList<Income> data = FXCollections.observableArrayList();
        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);

            while (rs.next()) {
                data.add(new Income(
                        "Type goes here",
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getString("currency"),
                        rs.getString("payment_type"),
                        rs.getString("taken_from"),
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
