package cn.ChengZhiYa.MHDFTools.Utils.Database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static cn.ChengZhiYa.MHDFTools.MHDFTools.dataSource;

public final class HomeUtil {
    static Map<Object, List<String>> HomeListHashMap = new HashMap<>();
    static Map<Object, Location> HomeLocationHashMap = new HashMap<>();

    public static Map<Object, List<String>> getHomeListHashMap() {
        return HomeListHashMap;
    }

    public static Map<Object, Location> getHomeLocationHashMap() {
        return HomeLocationHashMap;
    }

    public static File getPlayerFile(String PlayerName) {
        return new File(MHDFTools.instance.getDataFolder() + "/HomeData", PlayerName + ".yml");
    }

    public static Boolean ifHomeExists(String PlayerName, String HomeName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM MHDFTools_Home WHERE Home = ? AND Owner = ? LIMIT 1");
                ps.setString(1, HomeName);
                ps.setString(2, PlayerName);
                ResultSet rs = ps.executeQuery();
                boolean 结果 = rs.next();
                rs.close();
                ps.close();
                connection.close();
                return 结果;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        YamlConfiguration HomeData = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
        return HomeData.getStringList(PlayerName + "_HomeList").contains(HomeName);
    }

    public static Integer getPlayerHomeTime(String PlayerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM MHDFTools_Home WHERE Owner = ?");
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
            File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
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
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getHomeListHashMap().get(PlayerName) == null) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM MHDFTools_Home WHERE Owner = ?");
                    ps.setString(1, PlayerName);
                    ResultSet rs = ps.executeQuery();
                    List<String> HomeList = new ArrayList<>();
                    while (rs.next()) {
                        HomeList.add(rs.getString("Home"));
                    }
                    rs.close();
                    ps.close();
                    connection.close();
                    getHomeListHashMap().put(PlayerName, HomeList);
                    return HomeList;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                return getHomeListHashMap().get(PlayerName);
            }
        } else {
            File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
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
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getHomeLocationHashMap().get(PlayerName + "|" + HomeName) == null) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM MHDFTools_Home WHERE Home = ? AND Owner = ? LIMIT 1");
                    ps.setString(1, HomeName);
                    ps.setString(2, PlayerName);
                    ResultSet rs = ps.executeQuery();
                    Location HomeLocation = null;
                    if (rs.next()) {
                        HomeLocation = new Location(Bukkit.getWorld(Objects.requireNonNull(rs.getString("World"))),
                                rs.getDouble("X"),
                                rs.getDouble("Y"),
                                rs.getDouble("Z"),
                                (float) rs.getDouble("Yaw"),
                                (float) rs.getDouble("Pitch"));
                    }
                    getHomeLocationHashMap().put(PlayerName + "|" + HomeName, HomeLocation);
                    rs.close();
                    ps.close();
                    connection.close();
                    return HomeLocation;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                return getHomeLocationHashMap().get(PlayerName + "|" + HomeName);
            }
        } else {
            try {
                File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
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
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                try {
                    List<String> HomeList = getPlayerHomeList(PlayerName);
                    HomeList.add(HomeName);
                    getHomeListHashMap().put(PlayerName, HomeList);
                    getHomeLocationHashMap().put(PlayerName + "|" + HomeName, HomeLocation);
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO MHDFTools_Home (Home, Owner, World, X, Y, Z, Yaw, Pitch) VALUES (?,?,?,?,?,?,?,?)");
                    ps.setString(1, HomeName);
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
            File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
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
            } catch (IOException ignored) {
            }
        }
    }

    public static void SetHome(String PlayerName, String HomeName, Location HomeLocation) {
        if (ifHomeExists(PlayerName, HomeName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    try {
                        getHomeLocationHashMap().put(PlayerName + "|" + HomeName, HomeLocation);
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("UPDATE MHDFTools_Home SET World = ?, X = ?, Y = ?, Z = ?, Yaw = ?, Pitch = ? WHERE Home = ? AND Owner = ?");
                        ps.setString(1, HomeLocation.getWorld().getName());
                        ps.setDouble(2, HomeLocation.getX());
                        ps.setDouble(3, HomeLocation.getY());
                        ps.setDouble(4, HomeLocation.getZ());
                        ps.setDouble(5, HomeLocation.getYaw());
                        ps.setDouble(6, HomeLocation.getPitch());
                        ps.setString(7, HomeName);
                        ps.setString(8, PlayerName);
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
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
        if (ifHomeExists(PlayerName, HomeName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    List<String> HomeList = getPlayerHomeList(PlayerName);
                    HomeList.remove(HomeName);
                    getHomeListHashMap().put(PlayerName, HomeList);
                    getHomeLocationHashMap().remove(PlayerName + "|" + HomeName);
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("DELETE FROM MHDFTools_Home WHERE Home = ? AND Owner = ?");
                        ps.setString(1, HomeName);
                        ps.setString(2, PlayerName);
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
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

    public static int getMaxHome(Player player) {
        List<Integer> MaxHomePermList = new ArrayList<>();
        for (PermissionAttachmentInfo permInfo : player.getEffectivePermissions()) {
            String perm = permInfo.getPermission();
            if (perm.startsWith("mhdftools.home.")) {
                MaxHomePermList.add(Integer.valueOf(perm.substring("mhdftools.home.".length())));
            }
        }
        if (!MaxHomePermList.isEmpty()) {
            MaxHomePermList.sort(Collections.reverseOrder());
            return MaxHomePermList.get(0);
        }
        return MHDFTools.instance.getConfig().getInt("HomeSystemSettings.MaxHomeTime");
    }
}
