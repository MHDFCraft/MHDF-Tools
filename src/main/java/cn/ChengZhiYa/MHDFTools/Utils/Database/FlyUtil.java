package cn.ChengZhiYa.MHDFTools.Utils.Database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.MHDFTools.dataSource;
import static cn.ChengZhiYa.MHDFTools.Utils.Database.DatabaseUtil.*;

public final class FlyUtil {
    public static List<String> InFlyList = new ArrayList<>();
    static HashMap<String, Integer> FlyTimeHashMap = new HashMap<>();

    public static HashMap<String, Integer> getFlyTimeHashMap() {
        return FlyTimeHashMap;
    }

    public static boolean AllowFly(String PlayerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getFlyTimeHashMap().get(PlayerName) == null) {
                return DataExists("MHDFTools_Fly", "PlayerName", PlayerName);
            } else {
                return getFlyTimeHashMap().get(PlayerName) == null;
            }
        } else {
            File File = new File(MHDFTools.instance.getDataFolder(), "Cache/FlyCache.yml");
            YamlConfiguration Data = YamlConfiguration.loadConfiguration(File);
            return Data.get(PlayerName) != null;
        }
    }

    public static int getFlyTime(String PlayerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getFlyTimeHashMap().get(PlayerName) == null) {
                int PlayerTime = (int) GetData("MHDFTools_Fly", "PlayerName", PlayerName, "Time");
                getFlyTimeHashMap().put(PlayerName, PlayerTime);
                return PlayerTime;
            } else {
                return getFlyTimeHashMap().get(PlayerName);
            }
        } else {
            File File = new File(MHDFTools.instance.getDataFolder(), "Cache/FlyCache.yml");
            YamlConfiguration Data = YamlConfiguration.loadConfiguration(File);
            return Data.getInt(PlayerName);
        }
    }

    public static void takeFlyTime(String PlayerName, int TakeTime) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                getFlyTimeHashMap().put(PlayerName, getFlyTimeHashMap().get(PlayerName) - TakeTime);
                Take("MHDF_Tools", "PlayerName", PlayerName, "Time", TakeTime);
            } else {
                File File = new File(MHDFTools.instance.getDataFolder(), "Cache/FlyCache.yml");
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
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                if (AllowFly(PlayerName)) {
                    removeFly(PlayerName);
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
                File File = new File(MHDFTools.instance.getDataFolder(), "Cache/FlyCache.yml");
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
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
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
                File File = new File(MHDFTools.instance.getDataFolder(), "Cache/FlyCache.yml");
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
