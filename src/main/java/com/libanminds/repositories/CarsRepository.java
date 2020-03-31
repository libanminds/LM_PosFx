package com.libanminds.repositories;

import com.libanminds.models.Car;
import com.libanminds.singletons.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CarsRepository {
    public static ObservableList<Car> getCars() {
        String query = "SELECT * FROM cars";

        return getCarsFromQuery(query);
    }

    public static ObservableList<Car> getCustomerCars(int ownerId) {
        String query = "SELECT * FROM cars WHERE owner_id = " + ownerId;

        return getCarsFromQuery(query);
    }

    public static ObservableList<Car> getCustomerCarsLike(int ownerId, String value) {
        String query = "SELECT * FROM cars where" +
                " owner_id = " + ownerId + " and (" +
                " make like '%" + value + "%' or" +
                " model like '%" + value + "%' or" +
                " vin like '%" + value + "%' or" +
                " plate like '%" + value + "%')";

        return getCarsFromQuery(query);
    }

    public static ObservableList<Car> getCarsLike(String value) {
        String query = "SELECT * FROM cars where" +
                " make like '%" + value + "%' or" +
                " model like '%" + value + "%' or" +
                " vin like '%" + value + "%' or" +
                " plate like '%" + value + "%'";

        return getCarsFromQuery(query);
    }

    public static boolean addCar(Car car) {
        String query = "INSERT INTO cars(owner_id,make,model,year,current_odometer,next_service_odometer,vin,plate) VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement statement = DBConnection.getInstance().getPreparedStatement(query);
            statement.setInt(1, car.getOwnerID());
            statement.setString(2, car.getMake());
            statement.setString(3, car.getModel());
            statement.setString(4, car.getYear());
            statement.setString(5, car.getCurrentOdometer());
            statement.setString(6, car.getNextServiceOdometer());
            statement.setString(7, car.getVin());
            statement.setString(8, car.getPlate());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateCar(Car car) {
        String query = "UPDATE cars SET make = ? , model = ? , year = ?, current_odometer = ?, next_service_odometer = ? , vin = ?, plate = ? where id = ?";

        try {
            PreparedStatement statement = DBConnection.getInstance().getPreparedStatement(query);
            statement.setString(1, car.getMake());
            statement.setString(2, car.getModel());
            statement.setString(3, car.getYear());
            statement.setString(4, car.getCurrentOdometer());
            statement.setString(5, car.getNextServiceOdometer());
            statement.setString(6, car.getVin());
            statement.setString(7, car.getPlate());
            statement.setInt(8, car.getID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteCar(Car car) {
        try {
            String query = "DELETE FROM cars where id = " + car.getID();
            Statement statement = DBConnection.getInstance().getStatement();
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static ObservableList<Car> getCarsFromQuery(String query) {
        ObservableList<Car> data = FXCollections.observableArrayList();

        try {
            Statement statement = DBConnection.getInstance().getStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.add(new Car(
                        rs.getInt("id"),
                        rs.getInt("owner_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getString("year"),
                        rs.getString("current_odometer"),
                        rs.getString("next_service_odometer"),
                        rs.getString("vin"),
                        rs.getString("plate")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;
    }
}
