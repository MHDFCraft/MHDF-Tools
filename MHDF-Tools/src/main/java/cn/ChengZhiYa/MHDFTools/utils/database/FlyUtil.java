package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import lombok.Getter;
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

import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataSource;

public final class FlyUtil {
    public static final List<String> flyList = new ArrayList<>();
    @Getter
    static final HashMap<String, Integer> FlyTimeHashMap = new HashMap<>();

    public static boolean AllowFly(String PlayerName) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getFlyTimeHashMap().get(PlayerName) == null) {
                return DatabaseUtil.dataExists("MHDFTools_Fly", "PlayerName", PlayerName);
            } else {
                return true;
            }
        } else {
            File File = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "Cache/FlyCache.yml");
            YamlConfiguration Data = YamlConfiguration.loadConfiguration(File);
            return Data.get(PlayerName) != null;
        }
    }

    public static int getFlyTime(String PlayerName) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getFlyTimeHashMap().get(PlayerName) == null) {
                try (Connection connection = dataSource.getConnection()) {
                    try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM MHDFTools_Fly WHERE PlayerName = ? LIMIT 1")) {
                        ps.setString(1, PlayerName);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                int Time = rs.getInt("Time");
                                getFlyTimeHashMap().put(PlayerName, Time);
                                return Time;
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                return getFlyTimeHashMap().get(PlayerName);
            }
        } else {
            File File = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "Cache/FlyCache.yml");
            YamlConfiguration Data = YamlConfiguration.loadConfiguration(File);
            return Data.getInt(PlayerName);
        }
        return -1;
    }

    public static void takeFlyTime(String PlayerName, int TakeTime) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                getFlyTimeHashMap().put(PlayerName, getFlyTimeHashMap().get(PlayerName) - TakeTime);
                DatabaseUtil.take("MHDFTools_Fly", "PlayerName", PlayerName, "Time", TakeTime);
            } else {
                File File = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "Cache/FlyCache.yml");
                YamlConfiguration Data = YamlConfiguration.loadConfiguration(File);
                Data.set(PlayerName, getFlyTime(PlayerName) - TakeTime);
                try {
                    Data.save(File);
                } catch (IOException ignored) {
                }
            }
        });
    }

    public static void addFly(String PlayerName, int Time) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                if (AllowFly(PlayerName)) {
                    getFlyTimeHashMap().remove(PlayerName);
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("DELETE FROM MHDFTools_Fly WHERE PlayerName = ?");
                        ps.setString(1, PlayerName);
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                FlyTimeHashMap.put(PlayerName, Time);
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO MHDFTools_Fly (PlayerName, Time) VALUES (?,?)");
                    ps.setString(1, PlayerName);
                    ps.setInt(2, Time);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                File File = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "Cache/FlyCache.yml");
                YamlConfiguration Data = YamlConfiguration.loadConfiguration(File);
                Data.set(PlayerName, Time);
                try {
                    Data.save(File);
                } catch (IOException ignored) {
                }
            }
        });
    }

    public static void removeFly(String PlayerName) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                getFlyTimeHashMap().remove(PlayerName);
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("DELETE FROM MHDFTools_Fly WHERE PlayerName = ?");
                    ps.setString(1, PlayerName);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                File File = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "Cache/FlyCache.yml");
                YamlConfiguration Data = YamlConfiguration.loadConfiguration(File);
                Data.set(PlayerName, null);
                try {
                    Data.save(File);
                } catch (IOException ignored) {
                }
            }
        });
    }
}