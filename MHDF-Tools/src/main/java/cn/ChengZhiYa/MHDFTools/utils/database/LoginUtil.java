package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
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

    static String LOGIN_TABLE = "mhdftools_login";

    public static boolean loginExists(String playerName) {
        if (isUsingMySQL()) {
            return dataExists(LOGIN_TABLE, "PlayerName", playerName);
        } else {
            File loginFile = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "LoginData.yml");
            YamlConfiguration passwordData = YamlConfiguration.loadConfiguration(loginFile);
            return passwordData.getString(playerName + "_Password") != null;
        }
    }

    public static boolean checkPassword(String playerName, String password) {
        if (!loginExists(playerName)) {
            return false;
        }

        if (isUsingMySQL()) {
            String query = "SELECT 1 FROM " + LOGIN_TABLE + " WHERE PlayerName = ? AND Password = ? LIMIT 1";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, playerName);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            File loginFile = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "LoginData.yml");
            YamlConfiguration passwordData = YamlConfiguration.loadConfiguration(loginFile);
            String storedPassword = passwordData.getString(playerName + "_Password");
            return password.equals(storedPassword);
        }
    }

    public static void register(String playerName, String password) {
        if (loginExists(playerName)) {
            return;
        }

        if (isUsingMySQL()) {
            Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(
                             "INSERT INTO " + LOGIN_TABLE + " (PlayerName, Password) VALUES (?, ?)")) {
                    preparedStatement.setString(1, playerName);
                    preparedStatement.setString(2, password);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            File loginFile = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "LoginData.yml");
            YamlConfiguration passwordData = YamlConfiguration.loadConfiguration(loginFile);
            passwordData.set(playerName + "_Password", password);
            try {
                passwordData.save(loginFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isUsingMySQL() {
        return Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL");
    }
}