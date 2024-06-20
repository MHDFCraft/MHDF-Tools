package cn.ChengZhiYa.MHDFTools.util.database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.ChengZhiYa.MHDFTools.MHDFTools.initializationYamlData;
import static cn.ChengZhiYa.MHDFTools.util.Util.i18n;
import static cn.ChengZhiYa.MHDFTools.util.database.DatabaseUtil.*;
import static cn.ChengZhiYa.MHDFTools.util.database.EconomyUtil.ifPlayerFileExists;
import static cn.ChengZhiYa.MHDFTools.util.database.EconomyUtil.setMoney;
import static cn.ChengZhiYa.MHDFTools.util.database.HomeUtil.*;
import static cn.ChengZhiYa.MHDFTools.util.database.LoginUtil.LoginExists;
import static cn.ChengZhiYa.MHDFTools.util.database.LoginUtil.Register;

public final class ConvertData {
    public static void MySQLToYAML(CommandSender sender) {
        sender.sendMessage(i18n("AdminCommands.convert.ConvertStart", "YAML"));
        {
            MHDFTools.instance.getConfig().set("DataSettings.Type", "YAML");
            MHDFTools.instance.saveConfig();
            MHDFTools.instance.reloadConfig();
        }
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            initializationYamlData();

            try {
                //转换登录数据
                {
                    if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_login");
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            File DataFile = new File(MHDFTools.instance.getDataFolder(), "LoginData.yml");
                            YamlConfiguration Data = YamlConfiguration.loadConfiguration(DataFile);
                            Data.set(rs.getString("PlayerName") + "_Password", rs.getString("Password"));
                            Data.save(DataFile);
                        }
                        rs.close();
                        ps.close();
                        connection.close();
                    }
                }
                sender.sendMessage(i18n("AdminCommands.convert.ConvertDone", "登录系统", "YAML"));

                //转换经济数据
                {
                    if (MHDFTools.instance.getConfig().getBoolean("EconomySettings.Enable")) {
                        if (MHDFTools.instance.getConfig().getBoolean("EconomySettings.Enable")) {
                            Connection connection = dataSource.getConnection();
                            PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_economy");
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                File DataFile = EconomyUtil.getPlayerFile(rs.getString("PlayerName"));
                                if (!DataFile.exists()) {
                                    DataFile.createNewFile();
                                }
                                YamlConfiguration Data = YamlConfiguration.loadConfiguration(DataFile);
                                Data.set("money", rs.getDouble("Money"));
                                Data.save(DataFile);
                            }
                            rs.close();
                            ps.close();
                            connection.close();
                        }
                    }
                }
                sender.sendMessage(i18n("AdminCommands.convert.ConvertDone", "经济系统", "YAML"));

                //转换家数据
                {
                    if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_home");
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String HomeName = rs.getString("Home");
                            File DataFile = HomeUtil.getPlayerFile(rs.getString("Owner"));
                            if (!DataFile.exists()) {
                                DataFile.createNewFile();
                            }
                            YamlConfiguration Data = YamlConfiguration.loadConfiguration(DataFile);
                            Data.set(HomeName + ".World", rs.getDouble("World"));
                            Data.set(HomeName + ".X", rs.getDouble("X"));
                            Data.set(HomeName + ".Y", rs.getDouble("Y"));
                            Data.set(HomeName + ".Z", rs.getDouble("Z"));
                            Data.set(HomeName + ".Yaw", rs.getDouble("Yaw"));
                            Data.set(HomeName + ".Pitch", rs.getDouble("Pitch"));
                            Data.save(DataFile);
                        }
                        rs.close();
                        ps.close();
                        connection.close();
                    }
                }
                sender.sendMessage(i18n("AdminCommands.convert.ConvertDone", "家系统", "YAML"));
            } catch (SQLException | IOException e) {
                sender.sendMessage(i18n("AdminCommands.convert.ConvertError"));
            }
        });
    }

    public static void YAMLToMySQL(CommandSender sender, String DatabaseHost, String Database, String User, String Password) {
        sender.sendMessage(i18n("AdminCommands.convert.ConvertStart", "MySQL"));
        {
            MHDFTools.instance.getConfig().set("DataSettings.Type", "MySQL");
            MHDFTools.instance.getConfig().set("DataSettings.Host", DatabaseHost);
            MHDFTools.instance.getConfig().set("DataSettings.Database", Database);
            MHDFTools.instance.getConfig().set("DataSettings.User", User);
            MHDFTools.instance.getConfig().set("DataSettings.Password", Password);
            MHDFTools.instance.saveConfig();
            MHDFTools.instance.reloadConfig();
        }
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
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

                //转换登录数据
                {
                    if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
                        File File = new File(MHDFTools.instance.getDataFolder(), "LoginData.yml");
                        YamlConfiguration Data = YamlConfiguration.loadConfiguration(File);
                        for (String Datas : Objects.requireNonNull(Data.getConfigurationSection("")).getKeys(false)) {
                            if (!LoginExists(sender.getName())) {
                                Register(Datas.replaceAll("_Password", ""), Data.getString(Datas));
                            } else {
                                Set("mhdftools.mhdftools_login", "PlayerName", sender.getName(), "Password", Data.getString(Datas));
                            }
                        }
                    }
                }
                sender.sendMessage(i18n("AdminCommands.convert.ConvertDone", "登录系统", "MySQL"));

                //转换经济数据
                {
                    if (MHDFTools.instance.getConfig().getBoolean("EconomySettings.Enable")) {
                        List<String> VaultDataList = new ArrayList<>();
                        try (Stream<Path> File = Files.walk(new File(MHDFTools.instance.getDataFolder(), "VaultData").toPath())) {
                            VaultDataList = File.filter(Files::isRegularFile).map(Path::toString).map(FileName -> FileName.replaceAll("plugins\\\\MHDF-Tools\\\\VaultData\\\\", "")).collect(Collectors.toList());
                        } catch (IOException ignored) {
                        }
                        for (String VaultDatas : VaultDataList) {
                            String PlayerName = VaultDatas.replaceAll(".yml", "");
                            YamlConfiguration Data = YamlConfiguration.loadConfiguration(EconomyUtil.getPlayerFile(PlayerName));
                            if (!ifPlayerFileExists(PlayerName)) {
                                Connection connection = dataSource.getConnection();
                                PreparedStatement ps = connection.prepareStatement("INSERT INTO mhdftools.mhdftools_economy (PlayerName, Money) VALUES (?,?)");
                                ps.setString(1, PlayerName);
                                ps.setDouble(2, Data.getDouble("money"));
                                ps.executeUpdate();
                                ps.close();
                                connection.close();
                            } else {
                                setMoney(PlayerName, Data.getDouble("money"));
                            }
                        }
                    }
                }
                sender.sendMessage(i18n("AdminCommands.convert.ConvertDone", "经济系统", "MySQL"));

                //转换家数据
                {
                    if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
                        List<String> HomeDataList = new ArrayList<>();
                        try (Stream<Path> File = Files.walk(new File(MHDFTools.instance.getDataFolder(), "HomeData").toPath())) {
                            HomeDataList = File.filter(Files::isRegularFile).map(Path::toString).map(FileName -> FileName.replaceAll("plugins\\\\MHDF-Tools\\\\HomeData\\\\", "")).collect(Collectors.toList());
                        } catch (IOException ignored) {
                        }
                        for (String HomeDatas : HomeDataList) {
                            String PlayerName = HomeDatas.replaceAll(".yml", "");
                            YamlConfiguration Data = YamlConfiguration.loadConfiguration(HomeUtil.getPlayerFile(PlayerName));

                            for (String Datas : Objects.requireNonNull(Data.getConfigurationSection("")).getKeys(false)) {
                                if (!ifHomeExists(PlayerName, Datas)) {
                                    AddHome(PlayerName, Datas, new Location(
                                            Bukkit.getWorld(Objects.requireNonNull(Data.getString(Datas + ".World"))),
                                            Data.getDouble(Datas + ".X"),
                                            Data.getDouble(Datas + ".Y"),
                                            Data.getDouble(Datas + ".Z"),
                                            (float) Data.getDouble(Datas + ".Yaw"),
                                            (float) Data.getDouble(Datas + ".Pitch")
                                    ));
                                } else {
                                    SetHome(PlayerName, Datas, new Location(
                                            Bukkit.getWorld(Objects.requireNonNull(Data.getString(Datas + ".World"))),
                                            Data.getDouble(Datas + ".X"),
                                            Data.getDouble(Datas + ".Y"),
                                            Data.getDouble(Datas + ".Z"),
                                            (float) Data.getDouble(Datas + ".Yaw"),
                                            (float) Data.getDouble(Datas + ".Pitch")
                                    ));
                                }
                            }
                        }
                    }
                }
                sender.sendMessage(i18n("AdminCommands.convert.ConvertDone", "家系统", "MySQL"));
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(i18n("AdminCommands.convert.ConvertError"));
            }
        });
    }
}
