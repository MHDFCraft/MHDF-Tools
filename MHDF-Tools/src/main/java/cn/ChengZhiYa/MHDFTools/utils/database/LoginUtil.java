package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataExists;
import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataSource;

public final class LoginUtil {
    public static Boolean loginExists(String playerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            return dataExists("mhdftools.mhdftools_login", "PlayerName", playerName);
        }
        File loginFile = new File(MHDFTools.instance.getDataFolder(), "LoginData.yml");
        YamlConfiguration passwordData = YamlConfiguration.loadConfiguration(loginFile);
        return passwordData.getString(playerName + "_Password") != null;
    }

    public static boolean checkPassword(String playerName, String password) {
        if (loginExists(playerName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_login WHERE PlayerName = ? AND Password = ? LIMIT 1");
                    preparedStatement.setString(1, playerName);
                    preparedStatement.setString(2, password);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    boolean dataExists = resultSet.next();
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                    return dataExists;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            File loginFile = new File(MHDFTools.instance.getDataFolder(), "LoginData.yml");
            YamlConfiguration passwordData = YamlConfiguration.loadConfiguration(loginFile);
            return Objects.equals(passwordData.get(playerName + "_Password"), password);
        }
        return false;
    }

    public static void register(String playerName, String password) {
        if (!loginExists(playerName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO mhdftools.mhdftools_login (PlayerName, Password) VALUES (?,?)");
                        preparedStatement.setString(1, playerName);
                        preparedStatement.setString(2, password);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File loginFile = new File(MHDFTools.instance.getDataFolder(), "LoginData.yml");
                YamlConfiguration passwordData = YamlConfiguration.loadConfiguration(loginFile);
                passwordData.set(playerName + "_Password", password);
                try {
                    passwordData.save(loginFile);
                } catch (IOException ignored) {
                }
            }
        }
    }
}