package cn.ChengZhiYa.MHDFTools.Utils.Database;

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

import static cn.ChengZhiYa.MHDFTools.Utils.Database.HomeUtil.*;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class ImportUtil {
    public static void ImportHuskHomesData(CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            sender.sendMessage(i18n("AdminCommands.import.ImportStart", "HuskHomes"));
            if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
                try {
                    File HuskHomesPluginDataHome = new File(MHDFTools.instance.getDataFolder().getAbsoluteFile().getParent() + "\\HuskHomes\\");
                    if (HuskHomesPluginDataHome.exists()) {
                        YamlConfiguration HuskHomesConfig = YamlConfiguration.loadConfiguration(new File(HuskHomesPluginDataHome,"config.yml"));
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
                                            AddHome(rs3.getString("server_name"), PlayerName, HomeName, new Location(
                                                    Bukkit.getWorld(rs3.getString("world_name")),
                                                    rs3.getDouble("x"),
                                                    rs3.getDouble("y"),
                                                    rs3.getDouble("z"),
                                                    rs3.getFloat("yaw"),
                                                    rs3.getFloat("pitch")
                                            ));
                                        } else {
                                            SetHome(rs3.getString("server_name"), PlayerName, HomeName, new Location(
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
                    }else {
                        sender.sendMessage(i18n("AdminCommands.import.PluginNotInstall","HuskHomes"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(i18n("AdminCommands.import.ImportError"));
                }
            }else {
                sender.sendMessage(i18n("AdminCommands.import.NotImport","Home"));
            }
            sender.sendMessage(i18n("AdminCommands.import.ImportDone", "HuskHomes"));
        });
    }

    public static void ImportCMIData(CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            sender.sendMessage(i18n("AdminCommands.import.ImportStart", "CMI"));
            if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
                try {
                    File HuskHomesPluginDataHome = new File(MHDFTools.instance.getDataFolder().getAbsoluteFile().getParent() + "\\CMI\\");
                    if (HuskHomesPluginDataHome.exists()) {
                        YamlConfiguration HuskHomesConfig = YamlConfiguration.loadConfiguration(new File(HuskHomesPluginDataHome,"Settings/DataBaseInfo.yml"));
                        HikariConfig config = new HikariConfig();
                        DataSource dataSource;
                        {
                            switch (Objects.requireNonNull(HuskHomesConfig.getString("storage.method")).toLowerCase(Locale.ROOT)) {
                                case "mysql":
                                    config.setJdbcUrl("jdbc:mysql://" + HuskHomesConfig.getString("mysql.hostname") + "/" + HuskHomesConfig.getString("mysql.database") + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
                                    config.setUsername(HuskHomesConfig.getString("mysql.username"));
                                    config.setPassword(HuskHomesConfig.getString("mysql.password"));
                                    break;
                                case "sqlite":
                                    config.setJdbcUrl("jdbc:sqlite:" + MHDFTools.instance.getDataFolder().getAbsoluteFile().getParent() + "\\CMI\\cmi.sqlite.db");
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
                            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users");
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                String PlayerName = rs.getString("username");
                                String[] HomeDatas = rs.getString("Homes").replaceAll("\\$-0%%",":").split(";");
                                for (String HomeData : HomeDatas) {
                                    String[] Data = HomeData.split(":");
                                    if (!ifHomeExists(PlayerName, Data[0])) {
                                        AddHome(PlayerName, Data[0], new Location(
                                                Bukkit.getWorld(Data[1]),
                                                Double.parseDouble(Data[2]),
                                                Double.parseDouble(Data[3]),
                                                Double.parseDouble(Data[4]),
                                                Float.parseFloat(Data[5]),
                                                Float.parseFloat(Data[6])
                                        ));
                                    } else {
                                        SetHome(PlayerName, Data[0], new Location(
                                                Bukkit.getWorld(Data[1]),
                                                Double.parseDouble(Data[2]),
                                                Double.parseDouble(Data[3]),
                                                Double.parseDouble(Data[4]),
                                                Float.parseFloat(Data[5]),
                                                Float.parseFloat(Data[6])
                                        ));
                                    }
                                }
                            }

                            rs.close();
                            ps.close();
                            connection.close();
                        }
                    }else {
                        sender.sendMessage(i18n("AdminCommands.import.PluginNotInstall","CMI"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(i18n("AdminCommands.import.ImportError"));
                }
            }else {
                sender.sendMessage(i18n("AdminCommands.import.NotImport","Home"));
            }
            sender.sendMessage(i18n("AdminCommands.import.ImportDone", "CMI"));
        });
    }
}
