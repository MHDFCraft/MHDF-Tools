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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataExists;
import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataSource;

public final class FlyUtil {
    public static final List<String> InFlyList = new ArrayList<>();
    static final HashMap<String, Integer> FlyTimeHashMap = new HashMap<>();
    private static final String DATA_TYPE_CONFIG_KEY = "DataSettings.Type";
    private static final String MYSQL_DATA_TYPE = "MySQL";
    private static final String CACHE_FILE_PATH = "Cache/FlyCache.yml";

    private static YamlConfiguration loadFlyCache() {
        return loadYamlConfiguration();
    }

    private static void saveFlyCache(YamlConfiguration config) {
        saveYamlConfiguration(config, new File(MHDFTools.instance.getDataFolder(), CACHE_FILE_PATH));
    }

    public static boolean allowFly(String playerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString(DATA_TYPE_CONFIG_KEY), MYSQL_DATA_TYPE)) {
            return getFlyTimeHashMap().get(playerName) == null || dataExists("MHDFTools_Fly", "PlayerName", playerName);
        } else {
            YamlConfiguration data = loadFlyCache();
            return data.get(playerName) != null;
        }
    }

    public static int getFlyTime(String playerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString(DATA_TYPE_CONFIG_KEY), MYSQL_DATA_TYPE)) {
            if (getFlyTimeHashMap().containsKey(playerName)) {
                return getFlyTimeHashMap().get(playerName);
            } else {
                int time = fetchFlyTimeFromDatabase(playerName);
                getFlyTimeHashMap().put(playerName, time);
                return time;
            }
        } else {
            YamlConfiguration data = loadFlyCache();
            return data.getInt(playerName);
        }
    }

    public static void addFlyTime(String playerName, int time) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString(DATA_TYPE_CONFIG_KEY), MYSQL_DATA_TYPE)) {
                if (allowFly(playerName)) {
                    removeFlyTime(playerName);
                }
                FlyTimeHashMap.put(playerName, time);
                insertIntoDatabase(playerName, time);
            } else {
                YamlConfiguration data = loadFlyCache();
                data.set(playerName, time);
                saveFlyCache(data);
            }
        });
    }

    public static void removeFlyTime(String playerName) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString(DATA_TYPE_CONFIG_KEY), MYSQL_DATA_TYPE)) {
                getFlyTimeHashMap().remove(playerName);
                deleteFromDatabase(playerName);
            } else {
                YamlConfiguration data = loadFlyCache();
                data.set(playerName, null);
                saveFlyCache(data);
            }
        });
    }

    private static int fetchFlyTimeFromDatabase(String playerName) {
        int time = -1;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT Time FROM MHDFTools_Fly WHERE PlayerName = ? LIMIT 1")) {

            ps.setString(1, playerName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    time = rs.getInt("Time");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return time;
    }

    private static void insertIntoDatabase(String playerName, int time) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO " + "MHDFTools_Fly" + " (PlayerName, Time) VALUES (?,?)");
            ps.setString(1, playerName);
            ps.setInt(2, time);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteFromDatabase(String keyValue) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM " + "MHDFTools_Fly" + " WHERE " + "PlayerName" + " = ?");
            ps.setString(1, keyValue);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static YamlConfiguration loadYamlConfiguration() {
        File file = new File(MHDFTools.instance.getDataFolder(), FlyUtil.CACHE_FILE_PATH);
        return YamlConfiguration.loadConfiguration(file);
    }

    private static void saveYamlConfiguration(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException ignored) {
        }
    }

    public static HashMap<String, Integer> getFlyTimeHashMap() {
        return FlyUtil.FlyTimeHashMap;
    }
}