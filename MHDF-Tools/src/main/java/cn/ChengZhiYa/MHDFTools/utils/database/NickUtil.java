package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.*;

public final class NickUtil {

    public static Boolean ifNickExists(String playerName) {
        String dataType = PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type");
        if (Objects.equals(dataType, "MySQL")) {
            return dataExists("mhdftools_nick", "PlayerName", playerName);
        } else {
            File file = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "NickData.yml");
            return YamlConfiguration.loadConfiguration(file).getString(playerName) != null;
        }
    }

    public static String getNickName(String playerName) {
        String dataType = PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type");
        if (Objects.equals(dataType, "MySQL")) {
            Object nickName = getData("mhdftools_nick", "PlayerName", playerName, "NickName");
            return nickName != null ? nickName.toString() : playerName;
        } else {
            File file = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "NickData.yml");
            YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
            return data.getString(playerName, playerName);
        }
    }

    public static void setNickName(String playerName, String nickName) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            String dataType = PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type");
            if (Objects.equals(dataType, "MySQL")) {
                try (Connection connection = dataSource.getConnection()) {
                    String query = ifNickExists(playerName)
                            ? "UPDATE mhdftools_nick SET NickName = ? WHERE PlayerName = ?"
                            : "INSERT INTO mhdftools_nick (PlayerName, NickName) VALUES (?, ?)";
                    try (PreparedStatement ps = connection.prepareStatement(query)) {
                        ps.setString(1, nickName);
                        ps.setString(2, playerName);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                File file = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "NickData.yml");
                YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
                data.set(playerName, nickName);
                try {
                    data.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void removeNickName(String playerName) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            String dataType = PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type");
            if (Objects.equals(dataType, "MySQL")) {
                if (ifNickExists(playerName)) {
                    try (Connection connection = dataSource.getConnection()) {
                        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM mhdftools_nick WHERE PlayerName = ?")) {
                            ps.setString(1, playerName);
                            ps.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                File file = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "NickData.yml");
                YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
                data.set(playerName, null);
                try {
                    data.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}