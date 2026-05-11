package com.agroconecta.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/Agroconecta?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USER = "agro_backend";
    private static final String DEFAULT_PASSWORD = "agro_backend123";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = getEnvironmentValue("DB_URL", DEFAULT_URL);
        String user = getEnvironmentValue("DB_USER", DEFAULT_USER);
        String password = getEnvironmentValue("DB_PASSWORD", DEFAULT_PASSWORD);

        return DriverManager.getConnection(url, user, password);
    }

    private static String getEnvironmentValue(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
