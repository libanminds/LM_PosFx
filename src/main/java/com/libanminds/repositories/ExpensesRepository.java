package com.libanminds.repositories;

import com.libanminds.models.Customer;
import com.libanminds.models.Expense;
import com.libanminds.models.User;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
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

    public static boolean addExpense(Expense expense, int typeID) {
        String query = "INSERT INTO expenses(type_id,description,amount,currency,payment_type,recipient,notes) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setInt(1, typeID);
            statement.setString(2, expense.getDescription());
            statement.setDouble(3, expense.getAmount());
            statement.setString(4, expense.getCurrency());
            statement.setString(5, expense.getPaymentType());
            statement.setString(6, expense.getRecipient());
            statement.setString(7, expense.getNotes());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateExpense(Expense expense, int typeID) {
        String query = "UPDATE expenses SET type_id = ? , description = ? , amount = ?, currency = ?, payment_type = ? , recipient = ?, notes = ? where id = ?";

        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setInt(1, typeID);
            statement.setString(2, expense.getDescription());
            statement.setDouble(3, expense.getAmount());
            statement.setString(4, expense.getCurrency());
            statement.setString(5, expense.getPaymentType());
            statement.setString(6, expense.getRecipient());
            statement.setString(7, expense.getNotes());
            statement.setInt(8, expense.getID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean deleteExpense(Expense expense) {
        try {
            String query = "DELETE FROM expenses where id = " + expense.getID();
            Statement statement  = DBConnection.instance.getStatement();
            statement.executeUpdate(query);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private static ObservableList<Expense> getExpensesFromQuery(String query) {
        ObservableList<Expense> data = FXCollections.observableArrayList();
        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);

            while (rs.next()) {
                data.add(new Expense(
                        rs.getInt("id"),
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
