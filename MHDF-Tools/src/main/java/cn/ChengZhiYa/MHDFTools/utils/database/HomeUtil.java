package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
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

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.getHomeServerName;
import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataSource;

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
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT 1 FROM mhdftools.mhdftools_home WHERE Home = ? AND Owner = ? LIMIT 1")) {
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
            YamlConfiguration homeData = YamlConfiguration.loadConfiguration(getPlayerFile(playerName));
            return homeData.getStringList(playerName + "_HomeList").contains(homeName);
        }
    } //XD

    public static int getPlayerHomeTime(String playerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) AS TotalHomes FROM mhdftools.mhdftools_home WHERE Owner = ?")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("TotalHomes");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        } else {
            File homeDataFile = new File(MHDFTools.instance.getDataFolder(), "/HomeData/" + playerName + ".yml");
            if (!homeDataFile.exists()) {
                try {
                    homeDataFile.createNewFile();
                } catch (IOException ignored) {
                }
            }
            YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(homeDataFile);
            List<String> homeList = playerHomeData.getStringList(playerName + "_HomeList");
            return homeList.size();
        }
    }

    public static List<String> getPlayerHomeList(String playerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getHomeListHashMap().containsKey(playerName)) {
                return getHomeListHashMap().get(playerName);
            }

            List<String> homeList = new ArrayList<>();
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT Home FROM mhdftools.mhdftools_home WHERE Owner = ?")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        homeList.add(rs.getString("Home"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }

            getHomeListHashMap().put(playerName, homeList);
            return homeList;

        } else {
            File homeDataFolder = new File(MHDFTools.instance.getDataFolder(), "HomeData");
            File playerHomeFile = new File(homeDataFolder, playerName + ".yml");

            if (!playerHomeFile.exists()) {
                try {
                    playerHomeFile.createNewFile();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }

            YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(playerHomeFile);
            return new ArrayList<>(Objects.requireNonNull(playerHomeData.getKeys(false)));
        }
    }

    public static int getMaxHome(Player player) {
        int maxHome = MHDFTools.instance.getConfig().getInt("HomeSystemSettings.MaxHomeTime");

        for (PermissionAttachmentInfo permInfo : player.getEffectivePermissions()) {
            String perm = permInfo.getPermission();
            if (perm.startsWith("mhdftools.home.")) {
                try {
                    int value = Integer.parseInt(perm.substring("mhdftools.home.".length()));
                    if (value > maxHome) {
                        maxHome = value;
                    }
                } catch (NumberFormatException ignored) { //不是正常的数就执行
                }
            }
        }

        return maxHome;
    }

    public static String getHomeServer(String playerName, String homeName) {
        String server = getHomeServerHashMap().get(playerName + "|" + homeName).toString();
        if (server == null) {
            if (MHDFTools.instance.getConfig().getString("DataSettings.Type").equals("MySQL")) {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("SELECT Server FROM mhdftools.mhdftools_home WHERE Home = ? AND Owner = ? LIMIT 1")) {
                    ps.setString(1, homeName);
                    ps.setString(2, playerName);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            server = rs.getString("Server");
                            getHomeServerHashMap().put(playerName + "|" + homeName, server);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return server != null ? server : "NONE"; //别用中文了行不行
    }

    public static Location getHomeLocation(String playerName, String homeName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            try (Connection connection = dataSource.getConnection()) {
                //如果缓存则继续执行
                Location cachedLocation = getHomeLocationHashMap().get(playerName + "|" + homeName);
                if (cachedLocation != null) {
                    return cachedLocation;
                }

                PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_home WHERE Home = ? AND Owner = ? LIMIT 1");
                ps.setString(1, homeName);
                ps.setString(2, playerName);
                ResultSet rs = ps.executeQuery();

                Location homeLocation = null;
                if (rs.next()) {
                    homeLocation = new Location(
                            Bukkit.getWorld(Objects.requireNonNull(rs.getString("World"))),
                            rs.getDouble("X"),
                            rs.getDouble("Y"),
                            rs.getDouble("Z"),
                            (float) rs.getDouble("Yaw"),
                            (float) rs.getDouble("Pitch")
                    );
                    getHomeLocationHashMap().put(playerName + "|" + homeName, homeLocation); //缓存
                }

                rs.close();
                ps.close();

                return homeLocation;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File homeDataFile = new File(MHDFTools.instance.getDataFolder() + "/HomeData", playerName + ".yml"); //YML存储
                if (!homeDataFile.exists()) {
                    homeDataFile.createNewFile();
                }
                YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(homeDataFile);

                return new Location(
                        Bukkit.getWorld(Objects.requireNonNull(playerHomeData.getString(homeName + ".World"))),
                        playerHomeData.getDouble(homeName + ".X"),
                        playerHomeData.getDouble(homeName + ".Y"),
                        playerHomeData.getDouble(homeName + ".Z"),
                        (float) playerHomeData.getDouble(homeName + ".Yaw"),
                        (float) playerHomeData.getDouble(homeName + ".Pitch")
                );
            } catch (IOException ignored) {
            }
        }

        return null; // Return null if location not found or any exceptions occur
    }

    public static void addHome(String playerName, String homeName, Location homeLocation) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement(
                             "INSERT INTO mhdftools.mhdftools_home (Home, Owner, Server, World, X, Y, Z, Yaw, Pitch) VALUES (?,?,?,?,?,?,?,?,?)")) {

                    List<String> homeList = getPlayerHomeList(playerName);
                    homeList.add(homeName);
                    getHomeListHashMap().put(playerName, homeList);
                    getHomeLocationHashMap().put(playerName + "|" + homeName, homeLocation);
                    getHomeServerHashMap().put(playerName + "|" + homeName, getHomeServerName());

                    ps.setString(1, homeName);
                    ps.setString(2, playerName);
                    ps.setString(3, getHomeServerName());
                    ps.setString(4, homeLocation.getWorld().getName());
                    ps.setDouble(5, homeLocation.getX());
                    ps.setDouble(6, homeLocation.getY());
                    ps.setDouble(7, homeLocation.getZ());
                    ps.setDouble(8, homeLocation.getYaw());
                    ps.setDouble(9, homeLocation.getPitch());

                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            File homeData = new File(MHDFTools.instance.getDataFolder(), "/HomeData");
            File playerHomeFile = new File(homeData, playerName + ".yml");

            if (!playerHomeFile.exists()) {
                try {
                    playerHomeFile.createNewFile();
                } catch (IOException ignored) {
                }
            }

            YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(playerHomeFile);
            playerHomeData.set(homeName + ".World", homeLocation.getWorld().getName());
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

    public static void addHome(String serverName, String playerName, String homeName, Location homeLocation) {
        String dataSettingsType = MHDFTools.instance.getConfig().getString("DataSettings.Type");

        if (Objects.equals(dataSettingsType, "MySQL")) {
            Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                try (Connection connection = dataSource.getConnection()) {
                    List<String> homeList = getPlayerHomeList(playerName);
                    homeList.add(homeName);
                    getHomeListHashMap().put(playerName, homeList);
                    getHomeLocationHashMap().put(playerName + "|" + homeName, homeLocation);
                    getHomeServerHashMap().put(playerName + "|" + homeName, serverName);

                    String query = "INSERT INTO mhdftools.mhdftools_home (Home, Owner, Server, World, X, Y, Z, Yaw, Pitch) VALUES (?,?,?,?,?,?,?,?,?)";
                    try (PreparedStatement ps = connection.prepareStatement(query)) {
                        ps.setString(1, homeName);
                        ps.setString(2, playerName);
                        ps.setString(3, serverName);
                        ps.setString(4, homeLocation.getWorld().getName());
                        ps.setDouble(5, homeLocation.getX());
                        ps.setDouble(6, homeLocation.getY());
                        ps.setDouble(7, homeLocation.getZ());
                        ps.setDouble(8, homeLocation.getYaw());
                        ps.setDouble(9, homeLocation.getPitch());
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            addHome(playerName, homeName, homeLocation);
        }
    }

    public static void setHome(String playerName, String homeName, Location homeLocation) {
        if (!ifHomeExists(playerName, homeName)) {
            return;
        }

        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                try (Connection connection = dataSource.getConnection()) {
                    String updateQuery = "UPDATE mhdftools.mhdftools_home SET Server = ?, World = ?, X = ?, Y = ?, Z = ?, Yaw = ?, Pitch = ? WHERE Home = ? AND Owner = ?";
                    try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                        ps.setString(1, getHomeServerName());
                        ps.setString(2, homeLocation.getWorld().getName());
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
                    e.printStackTrace();
                }
            });
        } else {
            File homeData = new File(MHDFTools.instance.getDataFolder(), "/HomeData/" + playerName + ".yml");
            if (!homeData.exists()) {
                try {
                    homeData.getParentFile().mkdirs();
                    homeData.createNewFile();
                } catch (IOException ignored) {
                }
            }

            YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(homeData);
            playerHomeData.set(homeName + ".World", homeLocation.getWorld().getName());
            playerHomeData.set(homeName + ".X", homeLocation.getX());
            playerHomeData.set(homeName + ".Y", homeLocation.getY());
            playerHomeData.set(homeName + ".Z", homeLocation.getZ());
            playerHomeData.set(homeName + ".Yaw", homeLocation.getYaw());
            playerHomeData.set(homeName + ".Pitch", homeLocation.getPitch());

            try {
                playerHomeData.save(homeData);
            } catch (IOException ignored) {
            }
        }
    }

    public static void setHome(String ServerName, String PlayerName, String HomeName, Location HomeLocation) {
        if (ifHomeExists(PlayerName, HomeName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    try {
                        getHomeLocationHashMap().put(PlayerName + "|" + HomeName, HomeLocation);
                        getHomeServerHashMap().put(PlayerName + "|" + HomeName, getHomeServerName());
                        Connection connection = dataSource.getConnection();
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
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                HomeUtil.setHome(PlayerName, HomeName, HomeLocation);
            }
        }
    }

    public static void removeHome(String playerName, String homeName) {
        if (ifHomeExists(playerName, homeName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    List<String> homeList = getPlayerHomeList(playerName);
                    homeList.remove(homeName);
                    getHomeListHashMap().put(playerName, homeList);
                    getHomeLocationHashMap().remove(playerName + "|" + homeName);
                    getHomeServerHashMap().remove(playerName + "|" + homeName);
                    try (Connection connection = dataSource.getConnection();
                         PreparedStatement ps = connection.prepareStatement("DELETE FROM mhdftools.mhdftools_home WHERE Home = ? AND Owner = ?")) {
                        ps.setString(1, homeName);
                        ps.setString(2, playerName);
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File homeData = new File(MHDFTools.instance.getDataFolder() + "/HomeData");
                File homeDataFile = new File(homeData, playerName + ".yml");
                if (!homeDataFile.exists()) {
                    try {
                        homeDataFile.createNewFile();
                    } catch (IOException ignored) {
                    }
                }
                YamlConfiguration playerHomeData = YamlConfiguration.loadConfiguration(homeDataFile);
                playerHomeData.set(homeName, null);
                try {
                    playerHomeData.save(homeDataFile);
                } catch (IOException ignored) {
                }
            }
        }
    }
}