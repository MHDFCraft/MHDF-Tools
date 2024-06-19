package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdftools.MHDFTools;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.TimeZone;

import static cn.chengzhiya.mhdfpluginapi.Util.ColorLog;

public final class DatabaseUtil {
    public static Statement statement;
    public static HikariDataSource dataSource;

    public static void initializationDatabaseData() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + MHDFTools.instance.getConfig().getString("DataSettings.Host") + "/" + MHDFTools.instance.getConfig().getString("DataSettings.Database") + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
            config.setUsername(MHDFTools.instance.getConfig().getString("DataSettings.User"));
            config.setPassword(MHDFTools.instance.getConfig().getString("DataSettings.Password"));
            config.addDataSourceProperty("useUnicode", "true");
            config.addDataSourceProperty("characterEncoding", "utf8");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            dataSource = new HikariDataSource(config);
            statement = dataSource.getConnection().createStatement();
        } catch (SQLException ignored) {
            ColorLog("&c无法连接数据库");
        }
        try {
            //经济系统
            {
                if (MHDFTools.instance.getConfig().getBoolean("EconomySettings.Enable")) {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `mhdftools.mhdftools_economy` (" +
                            "`PlayerName` VARCHAR(50) NOT NULL DEFAULT ''," +
                            "`Money` DECIMAL(20,4) NOT NULL DEFAULT 0," +
                            "PRIMARY KEY (`PlayerName`)) " +
                            "COLLATE='utf8mb4_general_ci';");
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                }
            }
            //家系统
            {
                if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `mhdftools.mhdftools_home` (" +
                            "`ID` BIGINT NOT NULL AUTO_INCREMENT," +
                            "`Home` VARCHAR(100) NOT NULL DEFAULT ''," +
                            "`Owner` VARCHAR(50) NOT NULL DEFAULT ''," +
                            "`Server` VARCHAR(50) NOT NULL DEFAULT ''," +
                            "`World` VARCHAR(50) NOT NULL DEFAULT ''," +
                            "`X` DOUBLE NOT NULL DEFAULT 0," +
                            "`Y` DOUBLE NOT NULL DEFAULT 0," +
                            "`Z` DOUBLE NOT NULL DEFAULT 0," +
                            "`Yaw` DOUBLE NOT NULL DEFAULT 0," +
                            "`Pitch` DOUBLE NOT NULL DEFAULT 0," +
                            "PRIMARY KEY (`ID`)," +
                            "INDEX `Home` (`Home`)," +
                            "INDEX `Owner` (`Owner`)) " +
                            "COLLATE='utf8mb4_general_ci';");
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                }
            }
            //登录系统
            {
                if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `mhdftools.mhdftools_login` (" +
                            "`PlayerName` VARCHAR(50) NOT NULL DEFAULT ''," +
                            "`Password` VARCHAR(200) NOT NULL DEFAULT ''," +
                            "PRIMARY KEY (`PlayerName`)) " +
                            "COLLATE='utf8mb4_general_ci';");
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                }
            }
            //飞行系统
            {
                if (MHDFTools.instance.getConfig().getBoolean("FlySettings.Enable")) {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `MHDFTools_Fly` (" +
                            "`PlayerName` VARCHAR(50) NOT NULL DEFAULT ''," +
                            "`Time` INT NOT NULL DEFAULT 0," +
                            "PRIMARY KEY (`PlayerName`)) " +
                            "COLLATE='utf8mb4_general_ci';");
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeDatabase() {
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
            if (dataSource != null && !dataSource.isClosed()) {
                dataSource.close();
            }
        } catch (SQLException ignored) {
        }
    }

    public static boolean DataExists(String Table, String Field, String Value) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + Table + " WHERE " + Field + " = ? LIMIT 1");
            ps.setString(1, Value);
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

    public static Object GetData(String Table, String WhereField, String WhereValue, String GetField) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + Table + " WHERE " + WhereField + " = ? LIMIT 1");
            ps.setString(1, WhereValue);
            ResultSet rs = ps.executeQuery();
            Object Data = "";
            if (rs.next()) {
                Data = rs.getObject(GetField);
            }
            rs.close();
            ps.close();
            connection.close();
            return Data;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void Set(String Table, String WhereField, String WhereValue, String SetField, Object SetValue) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (DataExists(Table, WhereField, WhereValue)) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE " + Table + " SET " + SetField + " = ? WHERE " + WhereField + " = ?");
                    ps.setObject(1, SetValue);
                    ps.setString(2, WhereValue);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void Add(String Table, String WhereField, String WhereValue, String AddField, Object AddValue) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (DataExists(Table, WhereField, WhereValue)) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE " + Table + " SET " + AddField + " = " + AddField + "+? WHERE " + WhereField + " = ?");
                    ps.setObject(1, AddValue);
                    ps.setString(2, WhereValue);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void Take(String Table, String WhereField, String WhereValue, String TakeField, Object TakeValue) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (DataExists(Table, WhereField, WhereValue)) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE " + Table + " SET " + TakeField + " = " + TakeField + "-? WHERE " + WhereField + " = ?");
                    ps.setObject(1, TakeValue);
                    ps.setString(2, WhereValue);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
