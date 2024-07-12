package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
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
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            return dataExists("mhdftools_nick", "PlayerName", playerName);
        } else {
            File file = new File(MHDFTools.instance.getDataFolder(), "NickData.yml");
            YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
            return data.getString(playerName) != null;
        }
    }

    public static String getNickName(String playerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            Object nickName = getData("mhdftools_nick", "PlayerName", playerName, "NickName");
            return nickName != "" ? nickName.toString() : playerName;
        } else {
            File file = new File(MHDFTools.instance.getDataFolder(), "NickData.yml");
            YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
            return data.getString(playerName) != null ? data.getString(playerName) : playerName;
        }
    }

    public static void setNickName(String playerName, String nickName) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                if (ifNickExists(playerName)) {
                    try (Connection connection = dataSource.getConnection()) {
                        try (PreparedStatement ps = connection.prepareStatement("UPDATE mhdftools_nick SET NickName = ? WHERE PlayerName = ?")) {
                            ps.setString(1, nickName);
                            ps.setString(2, playerName);
                            ps.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    try (Connection connection = dataSource.getConnection()) {
                        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO mhdftools_nick (PlayerName, NickName) VALUES (?,?)")) {
                            ps.setString(1, playerName);
                            ps.setString(2, nickName);
                            ps.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                File file = new File(MHDFTools.instance.getDataFolder(), "NickData.yml");
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
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
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
                File file = new File(MHDFTools.instance.getDataFolder(), "NickData.yml");
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
