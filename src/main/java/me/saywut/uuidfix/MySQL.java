package me.saywut.uuidfix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private final String host = UUIDFix.getInstance().getConfig().getString("database.host");
    private final String port = UUIDFix.getInstance().getConfig().getString("database.port");
    private final String database = UUIDFix.getInstance().getConfig().getString("database.database_name");
    private final String username = UUIDFix.getInstance().getConfig().getString("database.username");
    private final String password = UUIDFix.getInstance().getConfig().getString("database.password");

    private Connection connection;

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" +
                        host + ":" + port + "/" + database + "?useSSL=false",
                        username, password);

    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
