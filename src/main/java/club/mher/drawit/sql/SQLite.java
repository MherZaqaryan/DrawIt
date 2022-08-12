package club.mher.drawit.sql;

import club.mher.drawit.DrawIt;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {

    private File cache = new File(DrawIt.getInstance().getDataFolder(), "cache.db");
    private final String url = "jdbc:sqlite:" + cache.getAbsolutePath();

    public Connection connection = null;

    public SQLite() {
        if (!cache.exists()) {
            try {
                cache.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(url);
            DrawIt.getInstance().getLogger().info("SQLite database successfully connected and established.");
        }catch (ClassNotFoundException | SQLException e) {
            DrawIt.getInstance().getLogger().info("Cant connect to SQLite database, something went wrong.");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
