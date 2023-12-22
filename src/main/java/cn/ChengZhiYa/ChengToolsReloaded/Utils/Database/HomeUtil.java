package cn.ChengZhiYa.ChengToolsReloaded.Utils.Database;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

import static cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded.dataSource;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.DatabaseUtil.DataExists;

public final class HomeUtil {

    public static File getPlayerFile(String PlayerName) {
        return new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData", PlayerName + ".yml");
    }

    public static Boolean HomeExists(String PlayerName, String HomeName) {
        if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            return DataExists("ChengTools_Home", "PlayerHome", PlayerName + "|" + HomeName);
        }
        YamlConfiguration HomeData = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
        return HomeData.getStringList(PlayerName + "_HomeList").contains(HomeName);
    }

    public static Integer getPlayerHomeTime(String PlayerName) {
        if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM ChengTools_Home WHERE Owner = ?");
                ps.setString(1, PlayerName);
                ResultSet rs = ps.executeQuery();
                int Data = 0;
                while (rs.next()) {
                    Data++;
                }
                rs.close();
                ps.close();
                connection.close();
                return Data;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File HomeData = new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData");
            File HomeData_File = new File(HomeData, PlayerName + ".yml");
            if (!HomeData_File.exists()) {
                try {
                    HomeData_File.createNewFile();
                } catch (IOException ignored) {
                }
            }
            YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
            List<String> HomeList = PlayerHomeData.getStringList(PlayerName + "_HomeList");
            return HomeList.size();
        }
        return 0;
    }

    public static List<String> getPlayerHomeList(String PlayerName) {
        if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM ChengTools_Home WHERE Owner = ?");
                ps.setString(1, PlayerName);
                ResultSet rs = ps.executeQuery();
                List<String> HomeList = new ArrayList<>();
                while (rs.next()) {
                    HomeList.add(rs.getString("PlayerHome").split("\\|")[1]);
                }
                rs.close();
                ps.close();
                connection.close();
                return HomeList;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File HomeData = new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData");
            File HomeData_File = new File(HomeData, PlayerName + ".yml");
            if (!HomeData_File.exists()) {
                try {
                    HomeData_File.createNewFile();
                } catch (IOException ignored) {
                }
            }
            YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
            return PlayerHomeData.getStringList(PlayerName + "_HomeList");
        }
        return new ArrayList<>();
    }

    public static Location getHome(String PlayerName, String HomeName) {
        if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM ChengTools_Home WHERE PlayerHome = ?");
                ps.setString(1, PlayerName + "|" + HomeName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new Location(Bukkit.getWorld(Objects.requireNonNull(rs.getString("World"))),
                            rs.getDouble("X"),
                            rs.getDouble("Y"),
                            rs.getDouble("Z"),
                            (float) rs.getDouble("Yaw"),
                            (float) rs.getDouble("Pitch"));
                }
                rs.close();
                ps.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File HomeData = new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData");
                File HomeData_File = new File(HomeData, PlayerName + ".yml");
                if (!HomeData_File.exists()) {
                    try {
                        HomeData_File.createNewFile();
                    } catch (IOException ignored) {
                    }
                }
                YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
                return new Location(Bukkit.getWorld(Objects.requireNonNull(PlayerHomeData.getString(HomeName + ".World"))),
                        PlayerHomeData.getDouble(HomeName + ".X"),
                        PlayerHomeData.getDouble(HomeName + ".Y"),
                        PlayerHomeData.getDouble(HomeName + ".Z"),
                        (float) PlayerHomeData.getDouble(HomeName + ".Yaw"),
                        (float) PlayerHomeData.getDouble(HomeName + ".Pitch"));
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static void AddHome(String PlayerName, String HomeName, Location HomeLocation) {
        if (!HomeExists(PlayerName, HomeName)) {
            if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(ChengToolsReloaded.instance, () -> {
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO ChengTools_Home (PlayerHome, Owner, World, X, Y, Z, Yaw, Pitch) VALUES (?,?,?,?,?,?,?,?)");
                        ps.setString(1, PlayerName + "|" + HomeName);
                        ps.setString(2, PlayerName);
                        ps.setString(3, HomeLocation.getWorld().getName());
                        ps.setDouble(4, HomeLocation.getX());
                        ps.setDouble(5, HomeLocation.getY());
                        ps.setDouble(6, HomeLocation.getZ());
                        ps.setDouble(7, HomeLocation.getYaw());
                        ps.setDouble(8, HomeLocation.getPitch());
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File HomeData = new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData");
                File HomeData_File = new File(HomeData, PlayerName + ".yml");
                if (!HomeData_File.exists()) {
                    try {
                        HomeData_File.createNewFile();
                    } catch (IOException ignored) {
                    }
                }
                YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
                List<String> HomeList = PlayerHomeData.getStringList(PlayerName + "_HomeList");
                HomeList.add(HomeName);
                PlayerHomeData.set(PlayerName + "_HomeList", HomeList);
                PlayerHomeData.set(HomeName + ".World", HomeLocation.getWorld().getName());
                PlayerHomeData.set(HomeName + ".X", HomeLocation.getX());
                PlayerHomeData.set(HomeName + ".Y", HomeLocation.getY());
                PlayerHomeData.set(HomeName + ".Z", HomeLocation.getZ());
                PlayerHomeData.set(HomeName + ".Yaw", HomeLocation.getYaw());
                PlayerHomeData.set(HomeName + ".Pitch", HomeLocation.getPitch());
                try {
                    PlayerHomeData.save(HomeData_File);
                } catch (IOException ignored) {}
            }
        } else {
            SetHome(PlayerName, HomeName, HomeLocation);
        }
    }

    public static void SetHome(String PlayerName, String HomeName, Location HomeLocation) {
        if (HomeExists(PlayerName, HomeName)) {
            if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(ChengToolsReloaded.instance, () -> {
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("UPDATE ChengTools_Home SET World = ?, X = ?, Y = ?, Z = ?, Yaw = ?, Pitch = ? WHERE PlayerHome = ?");
                        ps.setString(1, HomeLocation.getWorld().getName());
                        ps.setDouble(2, HomeLocation.getX());
                        ps.setDouble(3, HomeLocation.getY());
                        ps.setDouble(4, HomeLocation.getZ());
                        ps.setDouble(5, HomeLocation.getYaw());
                        ps.setDouble(6, HomeLocation.getPitch());
                        ps.setString(7, PlayerName + "|" + HomeName);
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File HomeData = new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData");
                File HomeData_File = new File(HomeData, PlayerName + ".yml");
                if (!HomeData_File.exists()) {
                    try {
                        HomeData_File.createNewFile();
                    } catch (IOException ignored) {
                    }
                }
                YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
                PlayerHomeData.set(HomeName + ".World", HomeLocation.getWorld().getName());
                PlayerHomeData.set(HomeName + ".X", HomeLocation.getX());
                PlayerHomeData.set(HomeName + ".Y", HomeLocation.getY());
                PlayerHomeData.set(HomeName + ".Z", HomeLocation.getZ());
                PlayerHomeData.set(HomeName + ".Yaw", HomeLocation.getYaw());
                PlayerHomeData.set(HomeName + ".Pitch", HomeLocation.getPitch());
                try {
                    PlayerHomeData.save(HomeData_File);
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void RemoveHome(String PlayerName, String HomeName) {
        if (HomeExists(PlayerName, HomeName)) {
            if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(ChengToolsReloaded.instance, () -> {
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("DELETE FROM ChengTools_Home WHERE PlayerHome = ?");
                        ps.setString(1, PlayerName + "|" + HomeName);
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File HomeData = new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData");
                File HomeData_File = new File(HomeData, PlayerName + ".yml");
                if (!HomeData_File.exists()) {
                    try {
                        HomeData_File.createNewFile();
                    } catch (IOException ignored) {
                    }
                }
                YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
                List<String> HomeList = PlayerHomeData.getStringList(PlayerName + "_HomeList");
                HomeList.remove(HomeName);
                PlayerHomeData.set(PlayerName + "_HomeList", HomeList);
                PlayerHomeData.set(HomeName + ".World", null);
                PlayerHomeData.set(HomeName + ".X", null);
                PlayerHomeData.set(HomeName + ".Y", null);
                PlayerHomeData.set(HomeName + ".Z", null);
                PlayerHomeData.set(HomeName + ".Yaw", null);
                PlayerHomeData.set(HomeName + ".Pitch", null);
                PlayerHomeData.set(HomeName, null);
                try {
                    PlayerHomeData.save(HomeData_File);
                } catch (IOException ignored) {
                }
            }
        }
    }
}
