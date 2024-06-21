package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import lombok.Getter;
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

public final class HomeUtil {
    @Getter
    static final Map<Object, List<String>> HomeListHashMap = new HashMap<>();
    @Getter
    static final Map<Object, Location> HomeLocationHashMap = new HashMap<>();
    @Getter
    static final Map<Object, Object> HomeServerHashMap = new HashMap<>();

    public static File getPlayerFile(String PlayerName) {
        return new File(MHDFTools.instance.getDataFolder() + "/HomeData", PlayerName + ".yml");
    }

    public static boolean ifHomeExists(String playerName, String homeName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            try (Connection connection = DatabaseUtil.dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_home WHERE Home = ? AND Owner = ? LIMIT 1")) {

                ps.setString(1, homeName);
                ps.setString(2, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            YamlConfiguration homeData = YamlConfiguration.loadConfiguration(HomeUtil.getPlayerFile(playerName));
            List<String> homeList = homeData.getStringList(playerName + "_HomeList");
            return homeList.contains(homeName);
        }
    }

    public static Integer getPlayerHomeTime(String PlayerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            try {
                Connection connection = DatabaseUtil.dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_home WHERE Owner = ?");
                ps.setString(1, PlayerName);
                ResultSet rs = ps.executeQuery();
                int Data = 0;
                while (rs.next()) {
                    ++Data;
                }
                rs.close();
                ps.close();
                connection.close();
                return Data;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
            File HomeData_File = new File(HomeData, PlayerName + ".yml");
            if (!HomeData_File.exists()) {
                try {
                    HomeData_File.createNewFile();
                }
                catch (IOException rs) {
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
            if (HomeUtil.getHomeListHashMap().get(PlayerName) != null) return HomeUtil.getHomeListHashMap().get(PlayerName);
            try {
                Connection connection = DatabaseUtil.dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_home WHERE Owner = ?");
                ps.setString(1, PlayerName);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> HomeList = new ArrayList<>();
                while (rs.next()) {
                    HomeList.add(rs.getString("Home"));
                }
                rs.close();
                ps.close();
                connection.close();
                HomeUtil.getHomeListHashMap().put(PlayerName, HomeList);
                return HomeList;
            }
            catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<String>();
            }
        }
        File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
        File HomeData_File = new File(HomeData, PlayerName + ".yml");
        if (!HomeData_File.exists()) {
            try {
                HomeData_File.createNewFile();
            }
            catch (IOException rs) {
                // empty catch block
            }
        }
        YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
        return new ArrayList<>(Objects.requireNonNull(PlayerHomeData.getConfigurationSection("")).getKeys(false));
    }

    public static int getMaxHome(Player player) {
        ArrayList<Integer> MaxHomePermList = new ArrayList<>();
        for (PermissionAttachmentInfo permInfo : player.getEffectivePermissions()) {
            String perm = permInfo.getPermission();
            if (!perm.startsWith("mhdftools.home.")) continue;
            MaxHomePermList.add(Integer.valueOf(perm.substring("mhdftools.home.".length())));
        }
        if (!MaxHomePermList.isEmpty()) {
            MaxHomePermList.sort(Collections.reverseOrder());
            return MaxHomePermList.get(0);
        }
        return MHDFTools.instance.getConfig().getInt("HomeSystemSettings.MaxHomeTime");
    }

    public static String getHomeServer(String PlayerName, String HomeName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (HomeUtil.getHomeServerHashMap().get(PlayerName + "|" + HomeName) == null) {
                try {
                    Connection connection = DatabaseUtil.dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_home WHERE Home = ? AND Owner = ? LIMIT 1");
                    ps.setString(1, HomeName);
                    ps.setString(2, PlayerName);
                    ResultSet rs = ps.executeQuery();
                    String Server2 = null;
                    if (rs.next()) {
                        Server2 = rs.getString("Server");
                    }
                    HomeUtil.getHomeServerHashMap().put(PlayerName + "|" + HomeName, Server2);
                    rs.close();
                    ps.close();
                    connection.close();
                    return Server2;
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                return (String)HomeUtil.getHomeServerHashMap().get(PlayerName + "|" + HomeName);
            }
        }
        return "无";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Location getHomeLocation(String PlayerName, String HomeName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (HomeUtil.getHomeLocationHashMap().get(PlayerName + "|" + HomeName) != null) return HomeUtil.getHomeLocationHashMap().get(PlayerName + "|" + HomeName);
            try {
                Connection connection = DatabaseUtil.dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_home WHERE Home = ? AND Owner = ? LIMIT 1");
                ps.setString(1, HomeName);
                ps.setString(2, PlayerName);
                ResultSet rs = ps.executeQuery();
                Location HomeLocation = null;
                if (rs.next()) {
                    HomeLocation = new Location(Bukkit.getWorld(Objects.requireNonNull(rs.getString("World"))), rs.getDouble("X"), rs.getDouble("Y"), rs.getDouble("Z"), (float)rs.getDouble("Yaw"), (float)rs.getDouble("Pitch"));
                }
                HomeUtil.getHomeLocationHashMap().put(PlayerName + "|" + HomeName, HomeLocation);
                rs.close();
                ps.close();
                connection.close();
                return HomeLocation;
            }
            catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
            File HomeData_File = new File(HomeData, PlayerName + ".yml");
            if (!HomeData_File.exists()) {
                try {
                    HomeData_File.createNewFile();
                }
                catch (IOException rs) {
                    // empty catch block
                }
            }
            YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
            return new Location(Bukkit.getWorld(Objects.requireNonNull(PlayerHomeData.getString(HomeName + ".World"))), PlayerHomeData.getDouble(HomeName + ".X"), PlayerHomeData.getDouble(HomeName + ".Y"), PlayerHomeData.getDouble(HomeName + ".Z"), (float)PlayerHomeData.getDouble(HomeName + ".Yaw"), (float)PlayerHomeData.getDouble(HomeName + ".Pitch"));
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }

    public static void addHome(String PlayerName, String HomeName, Location HomeLocation) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                try {
                    List<String> HomeList = HomeUtil.getPlayerHomeList(PlayerName);
                    HomeList.add(HomeName);
                    HomeUtil.getHomeListHashMap().put(PlayerName, HomeList);
                    HomeUtil.getHomeLocationHashMap().put(PlayerName + "|" + HomeName, HomeLocation);
                    HomeUtil.getHomeServerHashMap().put(PlayerName + "|" + HomeName, BungeeCordUtil.getHomeServerName());
                    Connection connection = DatabaseUtil.dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO mhdftools.mhdftools_home (Home, Owner, Server, World, X, Y, Z, Yaw, Pitch) VALUES (?,?,?,?,?,?,?,?,?)");
                    ps.setString(1, HomeName);
                    ps.setString(2, PlayerName);
                    ps.setString(3, BungeeCordUtil.getHomeServerName());
                    ps.setString(4, HomeLocation.getWorld().getName());
                    ps.setDouble(5, HomeLocation.getX());
                    ps.setDouble(6, HomeLocation.getY());
                    ps.setDouble(7, HomeLocation.getZ());
                    ps.setDouble(8, HomeLocation.getYaw());
                    ps.setDouble(9, HomeLocation.getPitch());
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
            File HomeData_File = new File(HomeData, PlayerName + ".yml");
            if (!HomeData_File.exists()) {
                try {
                    HomeData_File.createNewFile();
                }
                catch (IOException iOException) {
                    // empty catch block
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
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    public static void addHome(String ServerName, String PlayerName, String HomeName, Location HomeLocation) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                try {
                    List<String> HomeList = HomeUtil.getPlayerHomeList(PlayerName);
                    HomeList.add(HomeName);
                    HomeUtil.getHomeListHashMap().put(PlayerName, HomeList);
                    HomeUtil.getHomeLocationHashMap().put(PlayerName + "|" + HomeName, HomeLocation);
                    HomeUtil.getHomeServerHashMap().put(PlayerName + "|" + HomeName, BungeeCordUtil.getHomeServerName());
                    Connection connection = DatabaseUtil.dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO mhdftools.mhdftools_home (Home, Owner, Server, World, X, Y, Z, Yaw, Pitch) VALUES (?,?,?,?,?,?,?,?,?)");
                    ps.setString(1, HomeName);
                    ps.setString(2, PlayerName);
                    ps.setString(3, ServerName);
                    ps.setString(4, HomeLocation.getWorld().getName());
                    ps.setDouble(5, HomeLocation.getX());
                    ps.setDouble(6, HomeLocation.getY());
                    ps.setDouble(7, HomeLocation.getZ());
                    ps.setDouble(8, HomeLocation.getYaw());
                    ps.setDouble(9, HomeLocation.getPitch());
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            HomeUtil.addHome(PlayerName, HomeName, HomeLocation);
        }
    }

    public static void setHome(String PlayerName, String HomeName, Location HomeLocation) {
        if (HomeUtil.ifHomeExists(PlayerName, HomeName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    try {
                        HomeUtil.getHomeLocationHashMap().put(PlayerName + "|" + HomeName, HomeLocation);
                        HomeUtil.getHomeServerHashMap().put(PlayerName + "|" + HomeName, BungeeCordUtil.getHomeServerName());
                        Connection connection = DatabaseUtil.dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("UPDATE mhdftools.mhdftools_home SET Server = ?, World = ?, X = ?, Y = ?, Z = ?, Yaw = ?, Pitch = ? WHERE Home = ? AND Owner = ?");
                        ps.setString(1, BungeeCordUtil.getHomeServerName());
                        ps.setString(2, HomeLocation.getWorld().getName());
                        ps.setDouble(3, HomeLocation.getX());
                        ps.setDouble(4, HomeLocation.getY());
                        ps.setDouble(5, HomeLocation.getZ());
                        ps.setDouble(6, HomeLocation.getYaw());
                        ps.setDouble(7, HomeLocation.getPitch());
                        ps.setString(8, HomeName);
                        ps.setString(9, PlayerName);
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
                File HomeData_File = new File(HomeData, PlayerName + ".yml");
                if (!HomeData_File.exists()) {
                    try {
                        HomeData_File.createNewFile();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
                PlayerHomeData.set(HomeName + ".World", HomeLocation.getWorld().getName());
                PlayerHomeData.set(HomeName + ".X", HomeLocation.getX());
                PlayerHomeData.set(HomeName + ".Y", HomeLocation.getY());
                PlayerHomeData.set(HomeName + ".Z", HomeLocation.getZ());
                PlayerHomeData.set(HomeName + ".Yaw", Float.valueOf(HomeLocation.getYaw()));
                PlayerHomeData.set(HomeName + ".Pitch", Float.valueOf(HomeLocation.getPitch()));
                try {
                    PlayerHomeData.save(HomeData_File);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }
    }

    public static void setHome(String ServerName, String PlayerName, String HomeName, Location HomeLocation) {
        if (HomeUtil.ifHomeExists(PlayerName, HomeName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    try {
                        HomeUtil.getHomeLocationHashMap().put(PlayerName + "|" + HomeName, HomeLocation);
                        HomeUtil.getHomeServerHashMap().put(PlayerName + "|" + HomeName, BungeeCordUtil.getHomeServerName());
                        Connection connection = DatabaseUtil.dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("UPDATE mhdftools.mhdftools_home SET Server = ?, World = ?, X = ?, Y = ?, Z = ?, Yaw = ?, Pitch = ? WHERE Home = ? AND Owner = ?");
                        ps.setString(1, ServerName);
                        ps.setString(2, HomeLocation.getWorld().getName());
                        ps.setDouble(3, HomeLocation.getX());
                        ps.setDouble(4, HomeLocation.getY());
                        ps.setDouble(5, HomeLocation.getZ());
                        ps.setDouble(6, HomeLocation.getYaw());
                        ps.setDouble(7, HomeLocation.getPitch());
                        ps.setString(8, HomeName);
                        ps.setString(9, PlayerName);
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                HomeUtil.setHome(PlayerName, HomeName, HomeLocation);
            }
        }
    }

    public static void removeHome(String PlayerName, String HomeName) {
        if (HomeUtil.ifHomeExists(PlayerName, HomeName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    List<String> HomeList = HomeUtil.getPlayerHomeList(PlayerName);
                    HomeList.remove(HomeName);
                    HomeUtil.getHomeListHashMap().put(PlayerName, HomeList);
                    HomeUtil.getHomeLocationHashMap().remove(PlayerName + "|" + HomeName);
                    HomeUtil.getHomeServerHashMap().remove(PlayerName + "|" + HomeName);
                    try {
                        Connection connection = DatabaseUtil.dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("DELETE FROM mhdftools.mhdftools_home WHERE Home = ? AND Owner = ?");
                        ps.setString(1, HomeName);
                        ps.setString(2, PlayerName);
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File HomeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
                File HomeData_File = new File(HomeData, PlayerName + ".yml");
                if (!HomeData_File.exists()) {
                    try {
                        HomeData_File.createNewFile();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
                PlayerHomeData.set(HomeName + ".World", null);
                PlayerHomeData.set(HomeName + ".X", null);
                PlayerHomeData.set(HomeName + ".Y", null);
                PlayerHomeData.set(HomeName + ".Z", null);
                PlayerHomeData.set(HomeName + ".Yaw", null);
                PlayerHomeData.set(HomeName + ".Pitch", null);
                PlayerHomeData.set(HomeName, null);
                try {
                    PlayerHomeData.save(HomeData_File);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }
    }
}
