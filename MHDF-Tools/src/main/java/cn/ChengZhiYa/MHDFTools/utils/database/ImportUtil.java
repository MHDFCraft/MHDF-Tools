package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.database.WarpUtil.*;

public final class ImportUtil {
    public static void importHuskHomesData(CommandSender sender) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            String pluginName = "HuskHomes";

            File pluginDataFolder = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder().getParent(), pluginName);
            if (pluginDataFolder.exists()) {
                YamlConfiguration pluginConfig = YamlConfiguration.loadConfiguration(new File(pluginDataFolder, "config.yml"));
                String storageType = pluginConfig.getString("storage.method", "").toLowerCase(Locale.ROOT);

                HikariDataSource dataSource;

                {
                    HikariConfig config = new HikariConfig();
                    config.addDataSourceProperty("useUnicode", "true");
                    config.addDataSourceProperty("characterEncoding", "utf8");
                    config.addDataSourceProperty("cachePrepStmts", "true");
                    config.addDataSourceProperty("prepStmtCacheSize", "250");
                    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

                    switch (storageType) {
                        case "mysql":
                            config.setJdbcUrl("jdbc:mysql://" + pluginConfig.getString("database.credentials.host") + ":" + pluginConfig.getInt("database.credentials.port") + "/" + pluginConfig.getString("database.credentials.database") + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
                            config.setUsername(pluginConfig.getString("database.credentials.username"));
                            config.setPassword(pluginConfig.getString("database.credentials.password"));
                            break;
                        case "sqlite":
                            config.setJdbcUrl("jdbc:sqlite:" + pluginDataFolder + "/HuskHomesData.db");
                            config.setDriverClassName("org.sqlite.JDBC");
                            break;
                        default:
                            sender.sendMessage(i18n("AdminCommands.import.NotFoundDataType"));
                            return;
                    }
                    dataSource = new HikariDataSource(config);
                }

                if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("HomeSystemSettings.Enable")) {
                    sender.sendMessage(i18n("AdminCommands.import.ImportStart", pluginName, "Home"));
                    try (Connection connection = dataSource.getConnection();
                         PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + pluginConfig.getString("database.table_names.HOME_DATA"));
                         ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String ownerName = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("owner_uuid"))).getName();
                            int positionId = rs.getInt("saved_position_id");
                            try (Connection connection2 = dataSource.getConnection();
                                 PreparedStatement ps2 = connection2.prepareStatement("SELECT * FROM " + pluginConfig.getString("database.table_names.SAVED_POSITION_DATA") + " WHERE id = ?")) {
                                ps2.setInt(1, positionId);
                                try (ResultSet rs2 = ps2.executeQuery()) {
                                    if (rs2.next()) {
                                        String homeName = rs2.getString("name");
                                        int homeId = rs2.getInt("position_id");
                                        try (Connection connection3 = dataSource.getConnection();
                                             PreparedStatement ps3 = connection3.prepareStatement("SELECT * FROM " + pluginConfig.getString("database.table_names.SAVED_POSITION_DATA") + " WHERE id = ?")) {
                                            ps3.setInt(1, homeId);
                                            try (ResultSet rs3 = ps3.executeQuery()) {
                                                if (rs3.next()) {
                                                    String serverName = rs3.getString("server_name");
                                                    SuperLocation location = new SuperLocation(
                                                            rs3.getString("world_name"),
                                                            rs3.getDouble("x"),
                                                            rs3.getDouble("y"),
                                                            rs3.getDouble("z"),
                                                            rs3.getFloat("yaw"),
                                                            rs3.getFloat("pitch")
                                                    );
                                                    if (!ifHomeExists(homeName, serverName)) {
                                                        addHome(homeName, serverName, location);
                                                    } else {
                                                        setHome(homeName, serverName, location);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (SQLException e) {
                        i18n("AdminCommands.import.ImportError", pluginName, "Home");
                        throw new RuntimeException(e);
                    }
                    sender.sendMessage(i18n("AdminCommands.import.ImportDone", pluginName, "Home"));
                } else {
                    sender.sendMessage(i18n("AdminCommands.import.NotImport", "Home"));
                }
            } else {
                sender.sendMessage(i18n("AdminCommands.import.PluginNotInstall", pluginName));
            }
        });
    }

    public static void importCMIData(CommandSender sender) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            String pluginName = "CMI";

            File pluginDataFolder = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder().getParent(), pluginName);
            if (pluginDataFolder.exists()) {
                YamlConfiguration pluginConfig = YamlConfiguration.loadConfiguration(new File(pluginDataFolder, "Settings/DataBaseInfo.yml"));
                YamlConfiguration warpData = YamlConfiguration.loadConfiguration(new File(pluginDataFolder, "Saves/Warps.yml"));
                String storageType = pluginConfig.getString("storage.method", "").toLowerCase(Locale.ROOT);

                HikariDataSource dataSource;

                {
                    HikariConfig config = new HikariConfig();
                    config.addDataSourceProperty("useUnicode", "true");
                    config.addDataSourceProperty("characterEncoding", "utf8");
                    config.addDataSourceProperty("cachePrepStmts", "true");
                    config.addDataSourceProperty("prepStmtCacheSize", "250");
                    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

                    switch (storageType) {
                        case "mysql":
                            config.setJdbcUrl("jdbc:mysql://" + pluginConfig.getString("mysql.hostname") + "/" + pluginConfig.getString("mysql.database") + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
                            config.setUsername(pluginConfig.getString("mysql.username"));
                            config.setPassword(pluginConfig.getString("mysql.password"));
                            break;
                        case "sqlite":
                            config.setJdbcUrl("jdbc:sqlite:" + pluginDataFolder + "/cmi.sqlite.db");
                            config.setDriverClassName("org.sqlite.JDBC");
                            break;
                        default:
                            sender.sendMessage(i18n("AdminCommands.import.NotFoundDataType"));
                            return;
                    }
                    dataSource = new HikariDataSource(config);
                }

                if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("HomeSystemSettings.Enable")) {
                    sender.sendMessage(i18n("AdminCommands.import.ImportStart", pluginName, "Home"));
                    try (Connection connection = dataSource.getConnection();
                         PreparedStatement ps = connection.prepareStatement("SELECT * FROM users");
                         ResultSet rs = ps.executeQuery()) {

                        while (rs.next()) {
                            String playerName = rs.getString("username");
                            if (rs.getString("Homes") != null) {
                                String[] homeDataList = rs.getString("Homes").replaceAll("\\$-0%%", ":").split(";");
                                for (String homeData : homeDataList) {
                                    String[] data = homeData.split(":");
                                    SuperLocation location = new SuperLocation(
                                            data[1],
                                            Double.parseDouble(data[2]),
                                            Double.parseDouble(data[3]),
                                            Double.parseDouble(data[4]),
                                            Float.parseFloat(data[6]),
                                            Float.parseFloat(data[5])
                                    );
                                    if (!ifHomeExists(playerName, data[0])) {
                                        addHome(playerName, data[0], location);
                                    } else {
                                        setHome(playerName, data[0], location);
                                    }
                                }
                            }
                        }
                    } catch (SQLException e) {
                        i18n("AdminCommands.import.ImportError", pluginName, "Home");
                        throw new RuntimeException(e);
                    }
                    sender.sendMessage(i18n("AdminCommands.import.ImportDone", pluginName, "Home"));
                } else {
                    sender.sendMessage(i18n("AdminCommands.import.NotImport", "Home"));
                }
                if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("EconomySettings.Enable")) {
                    sender.sendMessage(i18n("AdminCommands.import.ImportStart", pluginName, "经济"));
                    try (Connection connection = dataSource.getConnection();
                         PreparedStatement ps = connection.prepareStatement("SELECT * FROM users");
                         ResultSet rs = ps.executeQuery()) {

                        while (rs.next()) {
                            String playerName = rs.getString("username");
                            double money = rs.getDouble("Balance");
                            if (EconomyUtil.ifPlayerFileExists(playerName)) {
                                EconomyUtil.addMoney(playerName, money);
                            } else {
                                EconomyUtil.initializationPlayerData(playerName, money);
                            }
                        }
                    } catch (SQLException e) {
                        i18n("AdminCommands.import.ImportError", pluginName, "经济");
                        throw new RuntimeException(e);
                    }
                    sender.sendMessage(i18n("AdminCommands.import.ImportDone", pluginName, "经济"));
                } else {
                    sender.sendMessage(i18n("AdminCommands.import.NotImport", "经济"));
                }
                if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("WarpSettings.Enable")) {
                    sender.sendMessage(i18n("AdminCommands.import.ImportStart", pluginName, "地标"));
                    for (String warpName : warpData.getKeys(false)) {
                        String[] data = Objects.requireNonNull(warpData.getString(warpName + ".Location")).split(";");
                        SuperLocation location = new SuperLocation(
                                data[0],
                                Double.parseDouble(data[1]),
                                Double.parseDouble(data[2]),
                                Double.parseDouble(data[3]),
                                Float.parseFloat(data[4]),
                                Float.parseFloat(data[5])
                        );
                        if (ifWarpExists(warpName)) {
                            setWarp(warpName,location);
                        }else {
                            addWarp(warpName,location);
                        }
                    }
                    sender.sendMessage(i18n("AdminCommands.import.ImportDone", pluginName, "地标"));
                }else {
                    sender.sendMessage(i18n("AdminCommands.import.NotImport", "地标"));
                }
            } else {
                sender.sendMessage(i18n("AdminCommands.import.PluginNotInstall", pluginName));
            }
        });
    }

    public static void importEssentialsData(CommandSender sender) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            String pluginName = "Essentials";

            File pluginDataFolder = new File(PluginLoader.INSTANCE.getPlugin().getDataFolder().getParent(), pluginName);
            if (pluginDataFolder.exists()) {
                File pluginUserDataFolder = new File(pluginDataFolder, "userdata");
                File warpDataFolder = new File(pluginDataFolder, "warps");
                if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("HomeSystemSettings.Enable")) {
                    sender.sendMessage(i18n("AdminCommands.import.ImportStart", pluginName, "Home"));
                    try (Stream<Path> pathStream = Files.walk(pluginUserDataFolder.toPath())) {
                        for (Path path : pathStream.filter(Files::isRegularFile).collect(Collectors.toList())) {
                            YamlConfiguration userData = YamlConfiguration.loadConfiguration(path.toFile());
                            String playerName = userData.getString("last-account-name");
                            if (userData.getConfigurationSection("homes") != null) {
                                for (String homeName : Objects.requireNonNull(userData.getConfigurationSection("homes")).getKeys(false)) {
                                    SuperLocation location = new SuperLocation(
                                            userData.getString("homes." + homeName + ".world-name"),
                                            userData.getDouble("homes." + homeName + ".x"),
                                            userData.getDouble("homes." + homeName + ".y"),
                                            userData.getDouble("homes." + homeName + ".z"),
                                            (float) userData.getDouble("homes." + homeName + ".yaw"),
                                            (float) userData.getDouble("homes." + homeName + ".pitch")
                                    );
                                    if (!ifHomeExists(playerName, homeName)) {
                                        addHome(playerName, homeName, location);
                                    } else {
                                        setHome(playerName, homeName, location);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        i18n("AdminCommands.import.ImportError", pluginName, "Home");
                        throw new RuntimeException(e);
                    }
                    sender.sendMessage(i18n("AdminCommands.import.ImportDone", pluginName, "Home"));
                } else {
                    sender.sendMessage(i18n("AdminCommands.import.NotImport", "Home"));
                }
                if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("EconomySettings.Enable")) {
                    sender.sendMessage(i18n("AdminCommands.import.ImportStart", pluginName, "经济"));
                    try (Stream<Path> pathStream = Files.walk(pluginUserDataFolder.toPath())) {
                        for (Path path : pathStream.filter(Files::isRegularFile).collect(Collectors.toList())) {
                            YamlConfiguration userData = YamlConfiguration.loadConfiguration(path.toFile());
                            String playerName = userData.getString("last-account-name");
                            double money = PluginLoader.INSTANCE.getPlugin().getConfig().getDouble("EconomySettings.InitialMoney");
                            if (userData.getString("money") != null) {
                                money = Double.parseDouble(Objects.requireNonNull(userData.getString("money")));
                            }
                            if (EconomyUtil.ifPlayerFileExists(playerName)) {
                                EconomyUtil.addMoney(playerName, money);
                            } else {
                                EconomyUtil.initializationPlayerData(playerName, money);
                            }
                        }
                    } catch (IOException e) {
                        i18n("AdminCommands.import.ImportError", pluginName, "经济");
                        throw new RuntimeException(e);
                    }
                    sender.sendMessage(i18n("AdminCommands.import.ImportDone", pluginName, "经济"));
                } else {
                    sender.sendMessage(i18n("AdminCommands.import.NotImport", "经济"));
                }
                if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("WarpSettings.Enable")) {
                    sender.sendMessage(i18n("AdminCommands.import.ImportStart", pluginName, "地标"));
                    try (Stream<Path> pathStream = Files.walk(warpDataFolder.toPath())) {
                        for (Path path : pathStream.filter(Files::isRegularFile).collect(Collectors.toList())) {
                            YamlConfiguration warpData = YamlConfiguration.loadConfiguration(path.toFile());
                            String warpName = warpData.getString("name");
                            String worldName = warpData.getString("world-name") != null ? warpData.getString("world-name") : warpData.getString("world");
                            SuperLocation location = new SuperLocation(
                                    worldName,
                                    warpData.getDouble("x"),
                                    warpData.getDouble("y"),
                                    warpData.getDouble("z"),
                                    (float) warpData.getDouble("yaw"),
                                    (float) warpData.getDouble("pitch")
                            );
                            if (ifWarpExists(warpName)) {
                                setWarp(warpName,location);
                            }else {
                                addWarp(warpName,location);
                            }
                        }
                    } catch (IOException e) {
                        i18n("AdminCommands.import.ImportError", pluginName, "地标");
                        throw new RuntimeException(e);
                    }
                    sender.sendMessage(i18n("AdminCommands.import.ImportDone", pluginName, "地标"));
                }else {
                    sender.sendMessage(i18n("AdminCommands.import.NotImport", "地标"));
                }
            } else {
                sender.sendMessage(i18n("AdminCommands.import.PluginNotInstall", pluginName));
            }
        });
    }
}