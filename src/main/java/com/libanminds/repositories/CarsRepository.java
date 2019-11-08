package com.libanminds.repositories;

import com.libanminds.models.Car;
import com.libanminds.models.Customer;
import com.libanminds.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CarsRepository {

    public static ObservableList<Car> getCars() {
        return getCarsFromQuery("SELECT * FROM cars");
    }

    public static ObservableList<Car> getCarsLike(String value) {
        String query = "SELECT * FROM cars where" +
                " model like '%" + value + "%' or" +
                " year like '%" + value + "%' or" +
                " vin like '%" + value + "%'";
        return getCarsFromQuery(query);
    }

    public static boolean addCar(Car car) {
        String query = "INSERT INTO cars(owner_id,model,year,vin,created_at,updated_at) VALUES (?,?,?,?)";
        PreparedStatement statement = DBConnection.instance.getPreparedStatement(query);
        try {
            statement.setInt(1, car.getOwnerId());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setString(4, car.getVin());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static ObservableList<Car> getCarsFromQuery(String query) {
        ObservableList<Car> data = FXCollections.observableArrayList();
        try {
            Statement statement = DBConnection.instance.getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Car(
                        rs.getInt("id"),
                        rs.getInt("owner_id"),
                        rs.getString("vin"),
                        rs.getString("model"),
                        rs.getInt("year")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
