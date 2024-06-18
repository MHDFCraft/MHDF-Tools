package cn.ChengZhiYa.MHDFTools.util.database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.TimeZone;

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

            initializeTable("EconomySettings",
                    "CREATE TABLE IF NOT EXISTS `mhdftools.mhdftools_economy` (" +
                            "`PlayerName` VARCHAR(50) NOT NULL DEFAULT ''," +
                            "`Money` DECIMAL(20,4) NOT NULL DEFAULT 0," +
                            "PRIMARY KEY (`PlayerName`)) " +
                            "COLLATE='utf8mb4_general_ci';");

            initializeTable("HomeSystemSettings",
                    "CREATE TABLE IF NOT EXISTS `mhdftools.mhdftools_home` (" +
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

            initializeTable("LoginSystemSettings",
                    "CREATE TABLE IF NOT EXISTS `mhdftools.mhdftools_login` (" +
                            "`PlayerName` VARCHAR(50) NOT NULL DEFAULT ''," +
                            "`Password` VARCHAR(200) NOT NULL DEFAULT ''," +
                            "PRIMARY KEY (`PlayerName`)) " +
                            "COLLATE='utf8mb4_general_ci';");

            initializeTable("FlySettings",
                    "CREATE TABLE IF NOT EXISTS `MHDFTools_Fly` (" +
                            "`PlayerName` VARCHAR(50) NOT NULL DEFAULT ''," +
                            "`Time` INT NOT NULL DEFAULT 0," +
                            "PRIMARY KEY (`PlayerName`)) " +
                            "COLLATE='utf8mb4_general_ci';");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static void initializeTable(String configKey, String sqlCreate) throws SQLException {
        if (MHDFTools.instance.getConfig().getBoolean(configKey + ".Enable")) {
            try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(sqlCreate)) {
                ps.executeUpdate();
            }
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

    public static boolean dataExists(String table, String field, String value) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + field + " = ? LIMIT 1")) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object getData(String table, String whereField, String whereValue, String getField) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + whereField + " = ? LIMIT 1")) {
            ps.setString(1, whereValue);
            try (ResultSet rs = ps.executeQuery()) {
                Object data = "";
                if (rs.next()) {
                    data = rs.getObject(getField);
                }
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void set(String table, String whereField, String whereValue, String setField, Object setValue) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (dataExists(table, whereField, whereValue)) {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement(
                             "UPDATE "
                                     + table
                                     + " SET "
                                     + setField
                                     + " = ? WHERE "
                                     + whereField
                                     + " = ?")) {
                    ps.setObject(1, setValue);
                    ps.setString(2, whereValue);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void add(String table, String whereField, String whereValue, String addField, Object addValue) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (dataExists(table, whereField, whereValue)) {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement(
                                 "UPDATE "
                                     + table
                                     + " SET "
                                     + addField
                                     + " = "
                                     + addField
                                     + "+? WHERE "
                                     + whereField
                                     + " = ?")) {
                    ps.setObject(1, addValue);
                    ps.setString(2, whereValue);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void take(String table, String whereField, String whereValue, String takeField, Object takeValue) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (dataExists(table, whereField, whereValue)) {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement ps = connection.prepareStatement(
                             "UPDATE "
                             + table
                             + " SET "
                             + takeField
                             + " = "
                             + takeField + "-? WHERE "
                             + whereField
                             + " = ?")) {
                    ps.setObject(1, takeValue);
                    ps.setString(2, whereValue);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}