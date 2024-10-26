package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
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
            return dataExists("MHDFTools_Nick", "PlayerName", playerName);
        } else {
            File file = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "NickData.yml");
            return YamlConfiguration.loadConfiguration(file).getString(playerName) != null;
        }
    }

    public static String getNickName(String playerName) {
        String dataType = PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type");
        if (Objects.equals(dataType, "MySQL")) {
            Object nickName = getData("MHDFTools_Nick", "PlayerName", playerName, "NickName");
            return nickName != null ? nickName.toString() : playerName;
        } else {
            File file = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "NickData.yml");
            YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
            return data.getString(playerName, playerName);
        }
    }

    public static void setNickName(String playerName, String nickName) {
        new FoliaScheduler(PluginLoader.INSTANCE.getPlugin()).runTaskAsynchronously(() -> {
            String dataType = PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type");
            if (Objects.equals(dataType, "MySQL")) {
                try (Connection connection = dataSource.getConnection()) {
                    String query = ifNickExists(playerName)
                            ? "UPDATE MHDFTools_Nick SET NickName = ? WHERE PlayerName = ?"
                            : "INSERT INTO MHDFTools_Nick (PlayerName, NickName) VALUES (?, ?)";
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
        new FoliaScheduler(PluginLoader.INSTANCE.getPlugin()).runTaskAsynchronously(() -> {
            String dataType = PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type");
            if (Objects.equals(dataType, "MySQL")) {
                if (ifNickExists(playerName)) {
                    try (Connection connection = dataSource.getConnection()) {
                        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM MHDFTools_Nick WHERE PlayerName = ?")) {
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