package me.MrIronMan.drawit.sql;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerData {

    private String table = "player_data";

    private Player player;
    private UUID uuid;

    public PlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public PlayerData() {
        createTable();
    }

    public void insertData() {
        if (playerExist()) return;
        try {
            PreparedStatement ps = getConn().prepareStatement("INSERT INTO  "+ table +"(`Player`, `UUID`, `Tokens`, `Points`, `GamesPlayed`, `Victories`, `CorrectGuesses`, `IncorrectGuesses`, `Skips`) VALUES (?,?,?,?,?,?,?,?,?)");
            ps.setString(1, player.getName());
            ps.setString(2, uuid.toString());
            for (int i = 3; i <= 9; i++) {
                ps.setInt(i, 0);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getData(PlayerDataType pdt) {
        if (!playerExist()) insertData();
        String dataType = PlayerDataType.getEnum(pdt);
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT " + dataType + " FROM " + table + " WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addData(PlayerDataType pdt, int value) {
        if (!playerExist()) insertData();
        String dataType = PlayerDataType.getEnum(pdt);
        try {
            PreparedStatement ps = getConn().prepareStatement("UPDATE " + table + " SET " + dataType + "=? WHERE UUID=?");
            ps.setInt(1, getData(pdt) + value);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        if (DrawIt.getConfigData().isMySql()) {
            return DrawIt.getInstance().getMySQL().getConnection();
        }else {
            return DrawIt.getInstance().getSqLite().getConnection();
        }
    }

    private void createTable() {
        try {
            PreparedStatement ps = getConn().prepareStatement("CREATE TABLE IF NOT EXISTS "+ table +" (" +
                    "Player VARCHAR(50)," +
                    "UUID VARCHAR(50)," +
                    "Tokens INT(50)," +
                    "Points INT(50)," +
                    "GamesPlayed INT(50)," +
                    "Victories INT(50)," +
                    "CorrectGuesses INT(50)," +
                    "IncorrectGuesses INT(50)," +
                    "Skips INT(50))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean playerExist() {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
