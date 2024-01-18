package cn.ChengZhiYa.MHDFTools.Utils.Database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.MHDFTools.dataSource;
import static cn.ChengZhiYa.MHDFTools.Utils.Database.DatabaseUtil.DataExists;
import static cn.ChengZhiYa.MHDFTools.Utils.Database.DatabaseUtil.GetData;

public final class LoginUtil {
    public static Boolean LoginExists(String PlayerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            return DataExists("MHDFTools_Login", "PlayerName", PlayerName);
        }
        File Login_File = new File(MHDFTools.instance.getDataFolder(), "LoginData.yml");
        YamlConfiguration PasswordData = YamlConfiguration.loadConfiguration(Login_File);
        return PasswordData.getString(PlayerName + "_Password") != null;
    }

    public static boolean CheckPassword(String PlayerName, String Password) {
        if (LoginExists(PlayerName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                return GetData("MHDFTools_Login", "PlayerName", PlayerName, Password).equals(Password);
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
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO MHDFTools_Login (PlayerName, Password) VALUES (?,?)");
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
