package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

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

public final class WarpUtil {
    @Getter
    static final Map<String, SuperLocation> warpLocationHashMap = new HashMap<>();
    @Getter
    static final Map<String, String> warpServerHashMap = new HashMap<>();
    private static final List<String> warpList = new ArrayList<>();

    public static void updateWarpList() {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools_warp");
                     ResultSet rs = ps.executeQuery()) {
                    warpList.clear();
                    while (rs.next()) {
                        warpList.add(rs.getString("Name"));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static List<String> getWarpList() {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            updateWarpList();
            return warpList;
        } else {
            YamlConfiguration warpData = YamlConfiguration.loadConfiguration(new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "WarpData.yml"));
            return new ArrayList<>(warpData.getKeys(false));
        }
    }

    public static boolean ifWarpExists(String warpName) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools_warp WHERE Name = ? LIMIT 1")) {
                ps.setString(1, warpName);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            YamlConfiguration warpData = YamlConfiguration.loadConfiguration(new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "WarpData.yml"));
            return warpData.getKeys(false).contains(warpName);
        }
    }

    public static String getWarpServer(String warpName) {
        String server = getWarpServerHashMap().get(warpName) != null ? getWarpServerHashMap().get(warpName) : "NONE";
        if (server.equals("NONE") && Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools_warp WHERE Name = ? LIMIT 1")) {
                ps.setString(1, warpName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String serverName = rs.getString("Server");
                        getWarpServerHashMap().put(warpName, serverName);
                        return serverName;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return server;
    }

    public static SuperLocation getWarp(String warpName) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getWarpLocationHashMap().get(warpName) == null) {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools_warp WHERE Name = ? LIMIT 1")) {
                    ps.setString(1, warpName);
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
                            getWarpLocationHashMap().put(warpName, homeLocation);
                            return homeLocation;
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return getWarpLocationHashMap().get(warpName);
            }
        } else {
            YamlConfiguration warpData = YamlConfiguration.loadConfiguration(new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "WarpData.yml"));
            return new SuperLocation(
                    warpData.getString(warpName + ".World"),
                    warpData.getDouble(warpName + ".X"),
                    warpData.getDouble(warpName + ".Y"),
                    warpData.getDouble(warpName + ".Z"),
                    (float) warpData.getDouble(warpName + ".Yaw"),
                    (float) warpData.getDouble(warpName + ".Pitch")
            );
        }
        return null;
    }

    public static void addWarp(String warpName, SuperLocation location) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                getServerName();
                getWarpLocationHashMap().put(warpName, location);
                getWarpServerHashMap().put(warpName, ServerName);
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement(
                             "INSERT INTO mhdftools_warp (Name, Server, World, X, Y, Z, Yaw, Pitch) VALUES (?,?,?,?,?,?,?,?)")) {

                    ps.setString(1, warpName);
                    ps.setString(2, ServerName);
                    ps.setString(3, location.getWorldName());
                    ps.setDouble(4, location.getX());
                    ps.setDouble(5, location.getY());
                    ps.setDouble(6, location.getZ());
                    ps.setDouble(7, location.getYaw());
                    ps.setDouble(8, location.getPitch());

                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                File warpDataFile = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "WarpData.yml");
                YamlConfiguration warpData = YamlConfiguration.loadConfiguration(warpDataFile);
                warpData.set(warpName + ".World", location.getWorldName());
                warpData.set(warpName + ".X", location.getX());
                warpData.set(warpName + ".Y", location.getY());
                warpData.set(warpName + ".Z", location.getZ());
                warpData.set(warpName + ".Yaw", location.getYaw());
                warpData.set(warpName + ".Pitch", location.getPitch());
                try {
                    warpData.save(warpDataFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void setWarp(String warpName, SuperLocation location) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                getServerName();
                getWarpLocationHashMap().put(warpName, location);
                getWarpServerHashMap().put(warpName, ServerName);
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement(
                             "UPDATE mhdftools_warp SET Server = ?, World = ?, X = ?, Y = ?, Z = ?, Yaw = ?, Pitch = ? WHERE Name = ?")) {

                    ps.setString(1, ServerName);
                    ps.setString(2, location.getWorldName());
                    ps.setDouble(3, location.getX());
                    ps.setDouble(4, location.getY());
                    ps.setDouble(5, location.getZ());
                    ps.setDouble(6, location.getYaw());
                    ps.setDouble(7, location.getPitch());
                    ps.setString(8, warpName);

                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                File warpDataFile = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "WarpData.yml");
                YamlConfiguration warpData = YamlConfiguration.loadConfiguration(warpDataFile);
                warpData.set(warpName + ".World", location.getWorldName());
                warpData.set(warpName + ".X", location.getX());
                warpData.set(warpName + ".Y", location.getY());
                warpData.set(warpName + ".Z", location.getZ());
                warpData.set(warpName + ".Yaw", location.getYaw());
                warpData.set(warpName + ".Pitch", location.getPitch());
                try {
                    warpData.save(warpDataFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void removeWarp(String warpName) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                getWarpLocationHashMap().remove(warpName);
                getWarpServerHashMap().remove(warpName);
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("DELETE FROM mhdftools_warp WHERE Name = ?")) {

                    ps.setString(1, warpName);

                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                File warpDataFile = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "WarpData.yml");
                YamlConfiguration warpData = YamlConfiguration.loadConfiguration(warpDataFile);
                warpData.set(warpName, null);
                try {
                    warpData.save(warpDataFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
