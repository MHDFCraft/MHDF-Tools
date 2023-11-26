package cn.ChengZhiYa.ChengToolsReloaded.Utils;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.DatabaseUtil.*;

public final class EconomyAPI {

    public static File getPlayerFile(String PlayerName) {
        return new File(ChengToolsReloaded.instance.getDataFolder() + "/VaultData", PlayerName + ".yml");
    }

    public static Boolean playerFileExists(String PlayerName) {
        if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            return DataExists("ChengTools_Economy", "PlayerName", PlayerName);
        }
        return getPlayerFile(PlayerName).exists();
    }

    public static Double getMoney(String PlayerName) {
        if (playerFileExists(PlayerName)) {
            if (Objects.equals(ChengToolsReloaded.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                GetData("ChengTools_Economy", "PlayerName", PlayerName, "Money");
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

    public static Boolean addTo(String PlayerName, Double amount) {
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

    public static Boolean takeFrom(String PlayerName, Double amount) {
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
