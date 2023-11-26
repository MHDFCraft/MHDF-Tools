package cn.ChengZhiYa.ChengToolsReloaded.Utils.Database;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded.dataSource;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.DatabaseUtil.*;

public final class EconomyUtil {

    public static File getPlayerFile(String PlayerName) {
        return new File(ChengToolsReloaded.instance.getDataFolder() + "/VaultData", PlayerName + ".yml");
    }

    public static Boolean playerFileExists(String PlayerName) {
        if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            return DataExists("ChengTools_Economy", "PlayerName", PlayerName);
        }
        return getPlayerFile(PlayerName).exists();
    }

    public static void initializationPlayerData(String PlayerName) {
        if (!playerFileExists(PlayerName)) {
            if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(ChengToolsReloaded.instance, () -> {
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO ChengTools_Economy (PlayerName, Money) VALUES (?,?)");
                        ps.setString(1,PlayerName);
                        ps.setDouble(2,ChengToolsReloaded.instance.getConfig().getDouble("EconomySettings.InitialMoney"));
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }else {
                File VaultFile = getPlayerFile(PlayerName);
                YamlConfiguration VaultData = YamlConfiguration.loadConfiguration(VaultFile);
                VaultData.set("money", ChengToolsReloaded.instance.getConfig().getDouble("EconomySettings.InitialMoney"));
                try {
                    VaultData.save(VaultFile);
                } catch (IOException ignored) {}
            }
        }
    }

    public static Double getMoney(String PlayerName) {
        if (playerFileExists(PlayerName)) {
            if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                return (Double) GetData("ChengTools_Economy", "PlayerName", PlayerName, "Money");
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
            return config.getDouble("money");
        }
        return -1.0;
    }

    public static void setMoney(String PlayerName, Double amount) {
        if (amount >= 0) {
            if (playerFileExists(PlayerName)) {
                if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                    SetData("ChengTools_Economy", "PlayerName", PlayerName, "Money", amount);
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
                if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                    Add("ChengTools_Economy", "PlayerName", PlayerName, "Money", amount);
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
                if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                    Take("ChengTools_Economy", "PlayerName", PlayerName, "Money", amount);
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
