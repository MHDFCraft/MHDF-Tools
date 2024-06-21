package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.*;

public final class ImportUtil {
    public static void importHuskHomesData(CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            sender.sendMessage(i18n("AdminCommands.import.ImportStart", "HuskHomes"));
            if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
                try {
                    File HuskHomesPluginDataHome = new File(MHDFTools.instance.getDataFolder().getAbsoluteFile().getParent() + "\\HuskHomes\\");
                    if (HuskHomesPluginDataHome.exists()) {
                        YamlConfiguration HuskHomesConfig = YamlConfiguration.loadConfiguration(new File(HuskHomesPluginDataHome, "config.yml"));
                        HikariConfig config = new HikariConfig();
                        DataSource dataSource;
                        {
                            switch (Objects.requireNonNull(HuskHomesConfig.getString("database.type")).toLowerCase(Locale.ROOT)) {
                                case "mysql":
                                case "mariadb":
                                    config.setJdbcUrl("jdbc:mysql://" + HuskHomesConfig.getString("database.credentials.host") + ":" + HuskHomesConfig.getInt("database.credentials.port") + "/" + HuskHomesConfig.getString("database.credentials.database") + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
                                    config.setUsername(HuskHomesConfig.getString("database.credentials.username"));
                                    config.setPassword(HuskHomesConfig.getString("database.credentials.password"));
                                    break;
                                case "h2":
                                case "sqlite":
                                    config.setJdbcUrl("jdbc:sqlite:" + MHDFTools.instance.getDataFolder().getAbsoluteFile().getParent() + "\\HuskHomes\\HuskHomesData.db");
                                    config.setDriverClassName("org.sqlite.JDBC");
                                    break;
                                default:
                                    sender.sendMessage(i18n("AdminCommands.import.NotFoundDataType"));
                                    return;
                            }
                            config.addDataSourceProperty("useUnicode", "true");
                            config.addDataSourceProperty("characterEncoding", "utf8");
                            config.addDataSourceProperty("cachePrepStmts", "true");
                            config.addDataSourceProperty("prepStmtCacheSize", "250");
                            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                            dataSource = new HikariDataSource(config);
                        }

                        {
                            Connection connection = dataSource.getConnection();
                            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + HuskHomesConfig.getString("database.table_names.HOME_DATA"));
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                String PlayerName = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("owner_uuid"))).getName();
                                int PositonId = rs.getInt("saved_position_id");

                                Connection connection2 = dataSource.getConnection();
                                PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM " + HuskHomesConfig.getString("database.table_names.SAVED_POSITION_DATA") + " WHERE id = ?");
                                ps2.setInt(1, PositonId);
                                ResultSet rs2 = ps2.executeQuery();
                                if (rs2.next()) {
                                    String HomeName = rs2.getString("name");
                                    int HomeId = rs2.getInt("position_id");

                                    Connection connection3 = dataSource.getConnection();
                                    PreparedStatement ps3 = connection.prepareStatement("SELECT * FROM " + HuskHomesConfig.getString("database.table_names.POSITION_DATA") + " WHERE id = ?");
                                    ps3.setInt(1, HomeId);
                                    ResultSet rs3 = ps3.executeQuery();

                                    if (rs3.next()) {
                                        if (!ifHomeExists(PlayerName, HomeName)) {
                                            addHome(rs3.getString("server_name"), PlayerName, HomeName, new Location(
                                                    Bukkit.getWorld(rs3.getString("world_name")),
                                                    rs3.getDouble("x"),
                                                    rs3.getDouble("y"),
                                                    rs3.getDouble("z"),
                                                    rs3.getFloat("yaw"),
                                                    rs3.getFloat("pitch")
                                            ));
                                        } else {
                                            HomeUtil.setHome(rs3.getString("server_name"), PlayerName, HomeName, new Location(
                                                    Bukkit.getWorld(rs3.getString("world_name")),
                                                    rs3.getDouble("x"),
                                                    rs3.getDouble("y"),
                                                    rs3.getDouble("z"),
                                                    rs3.getFloat("yaw"),
                                                    rs3.getFloat("pitch")
                                            ));
                                        }
                                    }

                                    rs3.close();
                                    ps3.close();
                                    connection3.close();
                                }
                                rs2.close();
                                ps2.close();
                                connection2.close();
                            }

                            rs.close();
                            ps.close();
                            connection.close();
                        }
                    } else {
                        sender.sendMessage(i18n("AdminCommands.import.PluginNotInstall", "HuskHomes"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(i18n("AdminCommands.import.ImportError"));
                }
            } else {
                sender.sendMessage(i18n("AdminCommands.import.NotImport", "Home"));
            }
            sender.sendMessage(i18n("AdminCommands.import.ImportDone", "HuskHomes"));
        });
    }

    public static void importCMIData(CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            String pluginName = "CMI";
            String importStartMsg = i18n("AdminCommands.import.ImportStart", pluginName);
            String pluginNotInstallMsg = i18n("AdminCommands.import.PluginNotInstall", pluginName);
            String importErrorMsg = i18n("AdminCommands.import.ImportError");
            String notImportMsg = i18n("AdminCommands.import.NotImport", "Home");
            String importDoneMsg = i18n("AdminCommands.import.ImportDone", pluginName);

            sender.sendMessage(importStartMsg);

            if (!MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
                sender.sendMessage(notImportMsg);
                return;
            }

            File huskHomesPluginDataHome = new File(MHDFTools.instance.getDataFolder().getParent(), "CMI");
            if (!huskHomesPluginDataHome.exists()) {
                sender.sendMessage(pluginNotInstallMsg);
                return;
            }

            try {
                YamlConfiguration huskHomesConfig = YamlConfiguration.loadConfiguration(new File(huskHomesPluginDataHome, "Settings/DataBaseInfo.yml"));
                String storageMethod = huskHomesConfig.getString("storage.method", "").toLowerCase(Locale.ROOT);

                HikariConfig config = new HikariConfig();
                DataSource dataSource;

                switch (storageMethod) {
                    case "mysql":
                        config.setJdbcUrl("jdbc:mysql://" + huskHomesConfig.getString("mysql.hostname") + "/" + huskHomesConfig.getString("mysql.database") + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
                        config.setUsername(huskHomesConfig.getString("mysql.username"));
                        config.setPassword(huskHomesConfig.getString("mysql.password"));
                        break;
                    case "sqlite":
                        config.setJdbcUrl("jdbc:sqlite:" + huskHomesPluginDataHome + "/cmi.sqlite.db");
                        config.setDriverClassName("org.sqlite.JDBC");
                        break;
                    default:
                        sender.sendMessage(i18n("AdminCommands.import.NotFoundDataType"));
                        return;
                }

                config.addDataSourceProperty("useUnicode", "true");
                config.addDataSourceProperty("characterEncoding", "utf8");
                config.addDataSourceProperty("cachePrepStmts", "true");
                config.addDataSourceProperty("prepStmtCacheSize", "250");
                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                dataSource = new HikariDataSource(config);

                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement("SELECT * FROM users");
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        String playerName = rs.getString("username");
                        if (rs.getString("Homes") != null) {
                            String[] homeDatas = rs.getString("Homes").replaceAll("\\$-0%%", ":").split(";");
                            for (String homeData : homeDatas) {
                                String[] data = homeData.split(":");
                                Location location = new Location(
                                        Bukkit.getWorld(data[1]),
                                        Double.parseDouble(data[2]),
                                        Double.parseDouble(data[3]),
                                        Double.parseDouble(data[4]),
                                        Float.parseFloat(data[5]),
                                        Float.parseFloat(data[6])
                                );
                                if (!ifHomeExists(playerName, data[0])) {
                                    addHome(playerName, data[0], location);
                                } else {
                                    setHome(playerName, data[0], location);
                                }
                            }
                        }
                    }
                }

                sender.sendMessage(importDoneMsg);
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(importErrorMsg);
            }
        });
    }
}