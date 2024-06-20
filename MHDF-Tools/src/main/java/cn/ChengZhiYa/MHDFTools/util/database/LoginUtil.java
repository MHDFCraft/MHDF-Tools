package cn.ChengZhiYa.MHDFTools.util.database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.database.DatabaseUtil.DataExists;
import static cn.ChengZhiYa.MHDFTools.util.database.DatabaseUtil.dataSource;

public final class LoginUtil {
    public static Boolean LoginExists(String PlayerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            return DataExists("mhdftools.mhdftools_login", "PlayerName", PlayerName);
        }
        File Login_File = new File(MHDFTools.instance.getDataFolder(), "LoginData.yml");
        YamlConfiguration PasswordData = YamlConfiguration.loadConfiguration(Login_File);
        return PasswordData.getString(PlayerName + "_Password") != null;
    }

    public static boolean CheckPassword(String PlayerName, String Password) {
        if (LoginExists(PlayerName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools.mhdftools_login WHERE PlayerName = ? AND Password = ? LIMIT 1");
                    ps.setString(1, PlayerName);
                    ps.setString(2, Password);
                    ResultSet rs = ps.executeQuery();
                    boolean Data = rs.next();
                    rs.close();
                    ps.close();
                    connection.close();
                    return Data;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            File Login_File = new File(MHDFTools.instance.getDataFolder(), "LoginData.yml");
            YamlConfiguration PasswordData = YamlConfiguration.loadConfiguration(Login_File);
            return Objects.equals(PasswordData.get(PlayerName + "_Password"), Password);
        }
        return false;
    }

    public static void Register(String PlayerName, String Password) {
        if (!LoginExists(PlayerName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO mhdftools.mhdftools_login (PlayerName, Password) VALUES (?,?)");
                        ps.setString(1, PlayerName);
                        ps.setString(2, Password);
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File Login_File = new File(MHDFTools.instance.getDataFolder(), "LoginData.yml");
                YamlConfiguration PasswordData = YamlConfiguration.loadConfiguration(Login_File);
                PasswordData.set(PlayerName + "_Password", Password);
                try {
                    PasswordData.save(Login_File);
                } catch (IOException ignored) {
                }
            }
        }
    }
}
