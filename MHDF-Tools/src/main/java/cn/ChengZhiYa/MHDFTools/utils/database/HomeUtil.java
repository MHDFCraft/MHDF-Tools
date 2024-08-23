package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.utils.file.FileCreator;
import lombok.Getter;
import org.bukkit.Bukkit;
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

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.ServerName;
import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.getServerName;
import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataSource;

public final class HomeUtil {
    @Getter
    static final Map<Object, List<String>> HomeListHashMap = new HashMap<>();
    @Getter
    static final Map<Object, SuperLocation> HomeLocationHashMap = new HashMap<>();
    @Getter
    static final Map<Object, Object> HomeServerHashMap = new HashMap<>();

    public static File getPlayerFile(String PlayerName) {
        return new File(PluginLoader.INSTANCE.getPlugin().getDataFolder() + "/HomeData", PlayerName + ".yml");
    }

    public static boolean ifHomeExists(String playerName, String homeName) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools_home WHERE Home = ? AND Owner = ? LIMIT 1")) {
                ps.setString(1, homeName);
                ps.setString(2, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            YamlConfiguration homeData = YamlConfiguration.loadConfiguration(getPlayerFile(playerName));
            return homeData.getKeys(false).contains(homeName);
        }
    }

    public static int getPlayerHomeTime(String playerName) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) AS TotalHomes FROM mhdftools_home WHERE Owner = ?")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("TotalHomes");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return 0;
        } else {
            File homeDataFile = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "/HomeData/" + playerName + ".yml");
            FileCreator.createFile(homeDataFile);
            YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(homeDataFile);
            List<String> homeList = playerHomeData.getStringList(playerName + "_HomeList");
            return homeList.size();
        }
    }

    public static List<String> getPlayerHomeList(String playerName) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getHomeListHashMap().containsKey(playerName)) {
                return getHomeListHashMap().get(playerName);
            }

            List<String> homeList = getHomeList(playerName);

            getHomeListHashMap().put(playerName, homeList);
            return homeList;

        } else {
            File homeDataFolder = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "HomeData");
            File playerHomeFile = new File(homeDataFolder, playerName + ".yml");
            FileCreator.createFile(playerHomeFile);
            YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(playerHomeFile);
            return new ArrayList<>(Objects.requireNonNull(playerHomeData.getKeys(false)));
        }
    }

    public static List<String> getHomeList(String playerName) {
        List<String> homeList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT Home FROM mhdftools_home WHERE Owner = ?")) {
            ps.setString(1, playerName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    homeList.add(rs.getString("Home"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return homeList;
    }

    public static int getMaxHome(Player player) {
        int maxHome = PluginLoader.INSTANCE.getPlugin().getConfig().getInt("HomeSystemSettings.MaxHomeTime");

        for (PermissionAttachmentInfo permInfo : player.getEffectivePermissions()) {
            String perm = permInfo.getPermission();
            if (perm.startsWith("mhdftools.home.")) {
                try {
                    int value = Integer.parseInt(perm.substring("mhdftools.home.".length()));
                    maxHome = Math.max(maxHome, value); //用Math.max代替if
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return maxHome;
    }

    public static String getHomeServer(String playerName, String homeName) {
        String server = getHomeServerHashMap().get(playerName + "|" + homeName) != null
                ? getHomeServerHashMap().get(playerName + "|" + homeName).toString() : "NONE";

        if (server.equals("NONE") && "MySQL".equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"))) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT Server FROM mhdftools_home WHERE Home = ? AND Owner = ? LIMIT 1")) {
                ps.setString(1, homeName);
                ps.setString(2, playerName);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        server = rs.getString("Server");
                        getHomeServerHashMap().put(playerName + "|" + homeName, server);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return server;
    }

    public static SuperLocation getHomeLocation(String playerName, String homeName) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            SuperLocation cachedLocation = HomeUtil.getHomeLocationHashMap().get(playerName + "|" + homeName);

            if (cachedLocation != null) { //如果有缓存那就执行
                return cachedLocation;
            }

            try (Connection connection = DatabaseUtil.dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools_home WHERE Home = ? AND Owner = ? LIMIT 1")) {
                ps.setString(1, homeName);
                ps.setString(2, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        SuperLocation homeLocation = new SuperLocation(
                                rs.getString("World"),
                                rs.getDouble("X"),
                                rs.getDouble("Y"),
                                rs.getDouble("Z"),
                                (float) rs.getDouble("Yaw"),
                                (float) rs.getDouble("Pitch")
                        );
                        HomeUtil.getHomeLocationHashMap().put(playerName + "|" + homeName, homeLocation);
                        return homeLocation;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            File homeData = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "HomeData");
            File playerHomeFile = new File(homeData, playerName + ".yml");
            FileCreator.createFile(playerHomeFile);
            YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(playerHomeFile);

            if (playerHomeData.contains(homeName + ".World")) {
                return new SuperLocation(
                        playerHomeData.getString(homeName + ".World"),
                        playerHomeData.getDouble(homeName + ".X"),
                        playerHomeData.getDouble(homeName + ".Y"),
                        playerHomeData.getDouble(homeName + ".Z"),
                        (float) playerHomeData.getDouble(homeName + ".Yaw"),
                        (float) playerHomeData.getDouble(homeName + ".Pitch")
                );
            }
        }
        return null;
    }

    public static void addHome(String playerName, String homeName, SuperLocation homeLocation) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
                getServerName();
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement(
                             "INSERT INTO mhdftools_home (Home, Owner, Server, World, X, Y, Z, Yaw, Pitch) VALUES (?,?,?,?,?,?,?,?,?)")) {
                    List<String> homeList = getPlayerHomeList(playerName);
                    homeList.add(homeName);
                    getHomeListHashMap().put(playerName, homeList);
                    getHomeLocationHashMap().put(playerName + "|" + homeName, homeLocation);
                    getHomeServerHashMap().put(playerName + "|" + homeName, ServerName);

                    ps.setString(1, homeName);
                    ps.setString(2, playerName);
                    ps.setString(3, ServerName);
                    ps.setString(4, homeLocation.getWorldName());
                    ps.setDouble(5, homeLocation.getX());
                    ps.setDouble(6, homeLocation.getY());
                    ps.setDouble(7, homeLocation.getZ());
                    ps.setDouble(8, homeLocation.getYaw());
                    ps.setDouble(9, homeLocation.getPitch());

                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            File homeData = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "/HomeData");
            File playerHomeFile = new File(homeData, playerName + ".yml");
            FileCreator.createFile(playerHomeFile);
            YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(playerHomeFile);

            setPlayerHomeData(homeName, homeLocation, playerHomeFile, playerHomeData);
        }
    }

    public static void addHome(String serverName, String playerName, String homeName, SuperLocation homeLocation) {
        String dataSettingsType = PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type");

        if (Objects.equals(dataSettingsType, "MySQL")) {
            Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
                try (Connection connection = dataSource.getConnection()) {
                    List<String> homeList = getPlayerHomeList(playerName);
                    homeList.add(homeName);
                    getHomeListHashMap().put(playerName, homeList);
                    getHomeLocationHashMap().put(playerName + "|" + homeName, homeLocation);
                    getHomeServerHashMap().put(playerName + "|" + homeName, serverName);

                    String query = "INSERT INTO mhdftools_home (Home, Owner, Server, World, X, Y, Z, Yaw, Pitch) VALUES (?,?,?,?,?,?,?,?,?)";
                    try (PreparedStatement ps = connection.prepareStatement(query)) {
                        ps.setString(1, homeName);
                        ps.setString(2, playerName);
                        ps.setString(3, serverName);
                        ps.setString(4, homeLocation.getWorldName());
                        ps.setDouble(5, homeLocation.getX());
                        ps.setDouble(6, homeLocation.getY());
                        ps.setDouble(7, homeLocation.getZ());
                        ps.setDouble(8, homeLocation.getYaw());
                        ps.setDouble(9, homeLocation.getPitch());
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            addHome(playerName, homeName, homeLocation);
        }
    }

    public static void setHome(String playerName, String homeName, SuperLocation homeLocation) {
        if (!ifHomeExists(playerName, homeName)) {
            return;
        }

        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
                getServerName();
                try (Connection connection = dataSource.getConnection()) {
                    String updateQuery = "UPDATE mhdftools_home SET Server = ?, World = ?, X = ?, Y = ?, Z = ?, Yaw = ?, Pitch = ? WHERE Home = ? AND Owner = ?";
                    try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                        ps.setString(1, ServerName);
                        ps.setString(2, homeLocation.getWorldName());
                        ps.setDouble(3, homeLocation.getX());
                        ps.setDouble(4, homeLocation.getY());
                        ps.setDouble(5, homeLocation.getZ());
                        ps.setDouble(6, homeLocation.getYaw());
                        ps.setDouble(7, homeLocation.getPitch());
                        ps.setString(8, homeName);
                        ps.setString(9, playerName);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            File homeData = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "/HomeData/" + playerName + ".yml");
            FileCreator.createDir(homeData.getParentFile());
            FileCreator.createFile(homeData);
            YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(homeData);

            setPlayerHomeData(homeName, homeLocation, homeData, playerHomeData);
        }
    }

    public static void setHome(String serverName, String playerName, String homeName, SuperLocation homeLocation) {
        if (!ifHomeExists(playerName, homeName)) {
            return;
        }

        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
                try {
                    //定义
                    String worldName = homeLocation.getWorldName();
                    double x = homeLocation.getX();
                    double y = homeLocation.getY();
                    double z = homeLocation.getZ();
                    double yaw = homeLocation.getYaw();
                    double pitch = homeLocation.getPitch();

                    //更新
                    try (Connection connection = dataSource.getConnection()) {
                        try (PreparedStatement ps = connection.prepareStatement("UPDATE mhdftools_home SET Server = ?, World = ?, X = ?, Y = ?, Z = ?, Yaw = ?, Pitch = ? WHERE Home = ? AND Owner = ?")) {
                            ps.setString(1, serverName);
                            ps.setString(2, worldName);
                            ps.setDouble(3, x);
                            ps.setDouble(4, y);
                            ps.setDouble(5, z);
                            ps.setDouble(6, yaw);
                            ps.setDouble(7, pitch);
                            ps.setString(8, homeName);
                            ps.setString(9, playerName);
                            ps.executeUpdate();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    getHomeLocationHashMap().put(playerName + "|" + homeName, homeLocation); //在内存中更新
                    getHomeServerHashMap().put(playerName + "|" + homeName, serverName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            HomeUtil.setHome(playerName, homeName, homeLocation);
        }
    }

    public static void removeHome(String playerName, String homeName) {
        if (ifHomeExists(playerName, homeName)) {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
                    List<String> homeList = getPlayerHomeList(playerName);
                    homeList.remove(homeName);
                    getHomeListHashMap().put(playerName, homeList);
                    getHomeLocationHashMap().remove(playerName + "|" + homeName);
                    getHomeServerHashMap().remove(playerName + "|" + homeName);
                    try (Connection connection = dataSource.getConnection();
                         PreparedStatement ps = connection.prepareStatement("DELETE FROM mhdftools_home WHERE Home = ? AND Owner = ?")) {
                        ps.setString(1, homeName);
                        ps.setString(2, playerName);
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                File homeData = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder() + "/HomeData");
                File homeDataFile = new File(homeData, playerName + ".yml");
                FileCreator.createFile(homeDataFile);
                YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(homeDataFile);

                playerHomeData.set(homeName, null);
                try {
                    playerHomeData.save(homeDataFile);
                } catch (IOException ignored) {
                }
            }
        }
    }

    private static void setPlayerHomeData(String homeName, SuperLocation homeLocation, File playerHomeFile, YamlConfiguration playerHomeData) {
        playerHomeData.set(homeName + ".World", homeLocation.getWorldName());
        playerHomeData.set(homeName + ".X", homeLocation.getX());
        playerHomeData.set(homeName + ".Y", homeLocation.getY());
        playerHomeData.set(homeName + ".Z", homeLocation.getZ());
        playerHomeData.set(homeName + ".Yaw", homeLocation.getYaw());
        playerHomeData.set(homeName + ".Pitch", homeLocation.getPitch());

        try {
            playerHomeData.save(playerHomeFile);
        } catch (IOException ignored) {
        }
    }
}