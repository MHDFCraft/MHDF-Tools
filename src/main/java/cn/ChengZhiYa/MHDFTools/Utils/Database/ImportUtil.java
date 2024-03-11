package cn.ChengZhiYa.MHDFTools.Utils.Database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;

import static cn.ChengZhiYa.MHDFTools.Utils.Database.HomeUtil.*;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class ImportUtil {
    public static void ImportHuskHomesMySQLData(CommandSender sender, String DatabaseHost, String Database, String User, String Password) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            sender.sendMessage(i18n("AdminCommands.import.ImportStart", "HuskHomes"));
            if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
                try {
                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl("jdbc:mysql://" + DatabaseHost + "/" + Database + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
                    config.setUsername(User);
                    config.setPassword(Password);
                    config.addDataSourceProperty("useUnicode", "true");
                    config.addDataSourceProperty("characterEncoding", "utf8");
                    config.addDataSourceProperty("cachePrepStmts", "true");
                    config.addDataSourceProperty("prepStmtCacheSize", "250");
                    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                    DataSource dataSource = new HikariDataSource(config);

                    {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("SELECT * FROM huskhomes_homes");
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String PlayerName = Bukkit.getOfflinePlayer(rs.getString("owner_uuid")).getName();
                            int PositonId = rs.getInt("saved_position_id");

                            Connection connection2 = dataSource.getConnection();
                            PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM huskhomes_saved_positions WHERE id = ?");
                            ps2.setInt(1, PositonId);
                            ResultSet rs2 = ps2.executeQuery();
                            if (rs2.next()) {
                                String HomeName = rs2.getString("name");
                                int HomeId = rs2.getInt("position_id");

                                Connection connection3 = dataSource.getConnection();
                                PreparedStatement ps3 = connection.prepareStatement("SELECT * FROM huskhomes_position_data WHERE id = ?");
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
                                        System.out.println(1);
                                    } else {
                                        SetHome(rs3.getString("server_name"), PlayerName, HomeName, new Location(
                                                Bukkit.getWorld(rs3.getString("world_name")),
                                                rs3.getDouble("x"),
                                                rs3.getDouble("y"),
                                                rs3.getDouble("z"),
                                                rs3.getFloat("yaw"),
                                                rs3.getFloat("pitch")
                                        ));
                                        System.out.println(2);
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
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(i18n("AdminCommands.import.ImportError"));
                }
            }
            sender.sendMessage(i18n("AdminCommands.import.ImportDone", "HuskHomes"));
        });
    }
}
