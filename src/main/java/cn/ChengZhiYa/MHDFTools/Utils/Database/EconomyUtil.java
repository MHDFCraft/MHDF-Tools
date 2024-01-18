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
import static cn.ChengZhiYa.MHDFTools.Utils.Database.DatabaseUtil.*;

public final class EconomyUtil {
    public static File getPlayerFile(String PlayerName) {
        return new File(MHDFTools.instance.getDataFolder() + "/VaultData", PlayerName + ".yml");
    }

    public static Boolean playerFileExists(String PlayerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            return DataExists("MHDFTools_Economy", "PlayerName", PlayerName);
        }
        return getPlayerFile(PlayerName).exists();
    }

    public static void initializationPlayerData(String PlayerName) {
        if (!playerFileExists(PlayerName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO MHDFTools_Economy (PlayerName, Money) VALUES (?,?)");
                        ps.setString(1, PlayerName);
                        ps.setDouble(2, MHDFTools.instance.getConfig().getDouble("EconomySettings.InitialMoney"));
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File VaultFile = getPlayerFile(PlayerName);
                YamlConfiguration VaultData = YamlConfiguration.loadConfiguration(VaultFile);
                VaultData.set("money", MHDFTools.instance.getConfig().getDouble("EconomySettings.InitialMoney"));
                try {
                    VaultData.save(VaultFile);
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static Double getMoney(String PlayerName) {
        if (playerFileExists(PlayerName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                return (Double) GetData("MHDFTools_Economy", "PlayerName", PlayerName, "Money");
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
            return config.getDouble("money");
        }
        return -1.0;
    }

    public static void setMoney(String PlayerName, Double amount) {
        if (amount >= 0) {
            if (playerFileExists(PlayerName)) {
                if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                    SetData("MHDFTools_Economy", "PlayerName", PlayerName, "Money", amount);
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
                config.set("money", amount);
                try {
                    config.save(getPlayerFile(PlayerName));
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static Boolean addMoney(String PlayerName, Double amount) {
        if (amount >= 0) {
            if (playerFileExists(PlayerName)) {
                if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                    Add("MHDFTools_Economy", "PlayerName", PlayerName, "Money", amount);
                    return true;
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
                config.set("money", getMoney(PlayerName) + amount);
                try {
                    config.save(getPlayerFile(PlayerName));
                    return true;
                } catch (IOException ignored) {
                }
            }
        }
        return false;
    }

    public static Boolean takeMoney(String PlayerName, Double amount) {
        if (amount >= 0) {
            if (playerFileExists(PlayerName)) {
                if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                    Take("MHDFTools_Economy", "PlayerName", PlayerName, "Money", amount);
                    return true;
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
                config.set("money", getMoney(PlayerName) - amount);
                try {
                    config.save(getPlayerFile(PlayerName));
                    return true;
                } catch (IOException ignored) {
                }
            }
        }
        return false;
    }
}
