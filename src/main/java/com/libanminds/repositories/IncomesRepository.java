package com.libanminds.repositories;

import com.libanminds.models.Expense;
import com.libanminds.models.Income;
import com.libanminds.models.Type;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IncomesRepository {

    public static ObservableList<Income> getIncomes() {
        String query = "SELECT * FROM incomes LEFT JOIN income_types ON incomes.type_id = income_types.id";

        return getIncomesFromQuery(query);
    }

    public static ObservableList<Income> getIncomesLike(String value) {
        String query = "SELECT * FROM incomes LEFT JOIN income_types ON incomes.type_id = income_types.id where" +
                " description like '%" + value + "%' or" +
                " taken_from like '%" + value + "%' or" +
                " name like '%" + value + "%' or" +
                " notes like '%"+ value + "%'";

        return getIncomesFromQuery(query);
    }

    public static boolean addIncome(Income income) {
        String query = "INSERT INTO incomes(type_id,description,amount,currency,payment_type,taken_from,notes) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setInt(1, income.getTypeObject().getID());
            statement.setString(2, income.getDescription());
            statement.setDouble(3, income.getAmount());
            statement.setString(4, income.getCurrency());
            statement.setString(5, income.getPaymentType());
            statement.setString(6, income.getFrom());
            statement.setString(7, income.getNotes());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateIncome(Income income) {
        String query = "UPDATE incomes SET type_id = ? , description = ? , amount = ?, currency = ?, payment_type = ? , taken_from = ?, notes = ? where id = ?";

        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setInt(1, income.getTypeObject().getID());
            statement.setString(2, income.getDescription());
            statement.setDouble(3, income.getAmount());
            statement.setString(4, income.getCurrency());
            statement.setString(5, income.getPaymentType());
            statement.setString(6, income.getFrom());
            statement.setString(7, income.getNotes());
            statement.setInt(8, income.getID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteIncome(Income income) {
        try {
            String query = "DELETE FROM incomes where id = " + income.getID();
            Statement statement  = DBConnection.instance.getStatement();
            statement.executeUpdate(query);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private static ObservableList<Income> getIncomesFromQuery(String query) {
        ObservableList<Income> data = FXCollections.observableArrayList();
        try {
            Statement statement  = DBConnection.instance.getStatement();
            ResultSet rs    = statement.executeQuery(query);

            while (rs.next()) {
                data.add(new Income(
                        rs.getInt(1),
                        new Type(rs.getInt("type_id"),rs.getString("name")),
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
