package com.libanminds.repositories;

import com.libanminds.enums.Currency;
import com.libanminds.utils.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SettingsRepository {
    private static final String VALUE_COL = "value";
    private static final String GENERAL_QUERY = "SELECT * FROM settings WHERE property = ";

    public static int usdToLbp() {
        return fetchIntSetting("usd_to_lbp", 0);
    }

    public static Currency defaultCurrency() {
        return Currency.fromString(fetchStringSetting("default_currency", "lbp"));
    }

    private static int fetchIntSetting(String property, int defaultVal) {
        try {
            Statement statement = DBConnection.instance.getStatement();
            try (ResultSet rs = statement.executeQuery(GENERAL_QUERY + property)) {
                return rs.next() ? rs.getInt(VALUE_COL) : defaultVal;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return defaultVal;
        }
    }

    private static boolean fetchBoolSetting(String property, boolean defaultVal) {
        try {
            Statement statement = DBConnection.instance.getStatement();
            try (ResultSet rs = statement.executeQuery(GENERAL_QUERY + property)) {
                return rs.next() ? rs.getBoolean(VALUE_COL) : defaultVal;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return defaultVal;
        }
    }

    private static String fetchStringSetting(String property, String defaultVal) {
        try {
            Statement statement = DBConnection.instance.getStatement();
            try (ResultSet rs = statement.executeQuery(GENERAL_QUERY + property)) {
                return rs.next() ? rs.getString(VALUE_COL) : defaultVal;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return defaultVal;
        }
    }
}
