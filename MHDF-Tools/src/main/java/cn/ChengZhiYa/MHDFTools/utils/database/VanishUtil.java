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

    private static YamlConfiguration loadVanishCache() {
        return loadYamlConfiguration();
    }

    private static YamlConfiguration loadYamlConfiguration() {
        File file = new File(MHDFTools.instance.getDataFolder(), CACHE_FILE_PATH);
        return YamlConfiguration.loadConfiguration(file);
    }

    private static void saveYamlConfiguration(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException ignored) {
        }
    }

    public static List<String> getVanishList() {
        if (Objects.equals(MHDFTools.instance.getConfig().getString(DATA_TYPE_CONFIG_KEY), MYSQL_DATA_TYPE)) {
            vanishList.clear();
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("SELECT PlayerName FROM MHDFTools_Vanis");) {
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            vanishList.add(rs.getString("PlayerName"));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return vanishList;
        } else {
            YamlConfiguration data = loadVanishCache();
            return data.getStringList(YAML_LIST_KEY);
        }
    }

    public static void addVanish(String playerName) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString(DATA_TYPE_CONFIG_KEY), MYSQL_DATA_TYPE)) {
                try (Connection connection = dataSource.getConnection()) {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO " + "MHDFTools_Vanish" + " (PlayerName) VALUES (?)");) {
                        ps.setString(1, playerName);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                YamlConfiguration data = loadVanishCache();
                vanishList.addAll(data.getStringList(DATA_TYPE_CONFIG_KEY));
                vanishList.add(playerName);
                data.set(YAML_LIST_KEY, vanishList);
                saveYamlConfiguration(data, new File(MHDFTools.instance.getDataFolder(), CACHE_FILE_PATH));
            }
        });
    }

    public static void removeVanish(String playerName) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString(DATA_TYPE_CONFIG_KEY), MYSQL_DATA_TYPE)) {
                try (Connection connection = dataSource.getConnection()) {
                    try (PreparedStatement ps = connection.prepareStatement("DELETE FROM MHDFTools_Vanish WHERE PlayerName = ?");) {
                        ps.setString(1, playerName);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                YamlConfiguration data = loadVanishCache();
                vanishList.addAll(data.getStringList(DATA_TYPE_CONFIG_KEY));
                vanishList.remove(playerName);
                data.set(YAML_LIST_KEY, vanishList);
                saveYamlConfiguration(data, new File(MHDFTools.instance.getDataFolder(), CACHE_FILE_PATH));
            }
        });
    }
}
