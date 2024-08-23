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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataSource;

public final class VanishUtil {
    private static final String DATA_TYPE_CONFIG_KEY = "DataSettings.Type";
    private static final String YAML_LIST_KEY = "VanishList";
    private static final String MYSQL_DATA_TYPE = "MySQL";
    private static final String CACHE_FILE_PATH = "Cache/VanishCache.yml";
    public static List<String> vanishList = new ArrayList<>();

    private static YamlConfiguration loadYamlConfiguration() {
        return YamlConfiguration.loadConfiguration(new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), CACHE_FILE_PATH));
    }

    private static void saveYamlConfiguration(YamlConfiguration config) {
        try {
            config.save(new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), CACHE_FILE_PATH));
        } catch (IOException ignored) {
        }
    }

    public static List<String> getVanishList() {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString(DATA_TYPE_CONFIG_KEY), MYSQL_DATA_TYPE)) {
            vanishList.clear();
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT PlayerName FROM MHDFTools_Vanish");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vanishList.add(rs.getString("PlayerName"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return vanishList;
        } else {
            YamlConfiguration data = loadYamlConfiguration();
            return data.getStringList(YAML_LIST_KEY);
        }
    }

    public static void addVanish(String playerName) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString(DATA_TYPE_CONFIG_KEY), MYSQL_DATA_TYPE)) {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("INSERT INTO MHDFTools_Vanish (PlayerName) VALUES (?)")) {
                    ps.setString(1, playerName);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                YamlConfiguration data = loadYamlConfiguration();
                List<String> vanishList = data.getStringList(YAML_LIST_KEY);
                vanishList.add(playerName);
                data.set(YAML_LIST_KEY, vanishList);
                saveYamlConfiguration(data);
            }
        });
    }

    public static void removeVanish(String playerName) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString(DATA_TYPE_CONFIG_KEY), MYSQL_DATA_TYPE)) {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("DELETE FROM MHDFTools_Vanish WHERE PlayerName = ?")) {
                    ps.setString(1, playerName);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                YamlConfiguration data = loadYamlConfiguration();
                List<String> vanishList = data.getStringList(YAML_LIST_KEY);
                vanishList.remove(playerName);
                data.set(YAML_LIST_KEY, vanishList);
                saveYamlConfiguration(data);
            }
        });
    }
}